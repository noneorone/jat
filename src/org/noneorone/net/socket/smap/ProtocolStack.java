package org.noneorone.net.socket.smap;

// $Id: ProtocolStack.java,v 1.27 2006/01/27 14:53:57 belaban Exp $

import java.util.*;

/**
 * A ProtocolStack manages a number of protocols layered above each other. It creates all
 * protocol classes, initializes them and, when ready, starts all of them, beginning with the
 * bottom most protocol. It also dispatches messages received from the stack to registered
 * objects (e.g. channel, GMP) and sends messages sent by those objects down the stack.<p>
 * The ProtocolStack makes use of the Configurator to setup and initialize stacks, and to
 * destroy them again when not needed anymore
 * @author Bela Ban
 */
public class ProtocolStack
    extends Protocol
{
  private Protocol top_prot = null;
  private Protocol bottom_prot = null;

  private String setup_string;

  //   private JChannel                channel=null;
  private boolean stopped = true;

  //   public final  TimeScheduler     timer=new TimeScheduler(60000);
  // final Promise                   ack_promise=new Promise();

  /** Used to sync on START/START_OK events for start()*/
  Promise                         start_promise=null;

  /** used to sync on STOP/STOP_OK events for stop() */
 Promise                         stop_promise=null;

  public static final int ABOVE = 1; // used by insertProtocol()
  public static final int BELOW = 2; // used by insertProtocol()

//    public ProtocolStack(JChannel channel, String setup_string) throws ChannelException {
//        this.setup_string=setup_string;
//        this.channel=channel;
//        ClassConfigurator.getInstance(true); // will create the singleton
//    }

  /** Only used by Simulator; don't use */
  public ProtocolStack()
  {

  }

//    public Channel getChannel() {
//        return channel;
//    }

  /** Returns all protocols in a list, from top to bottom. <em>These are not copies of protocols,
   so modifications will affect the actual instances !</em> */
  public Vector getProtocols()
  {
    Protocol p;
    Vector v = new Vector();

    p = top_prot;
    while (p != null)
    {
      v.addElement(p);
      p = p.getDownProtocol();
    }
    return v;
  }

  /**
   *
   * @return Map<String,Map<key,val>>
   */
  public Map dumpStats()
  {
    Protocol p;
    Map retval = new HashMap(), tmp;
    String prot_name;

    p = top_prot;
    while (p != null)
    {
      prot_name = p.getName();
      tmp = p.dumpStats();
      if (prot_name != null && tmp != null)
      {
        retval.put(prot_name, tmp);
      }
      p = p.getDownProtocol();
    }
    return retval;
  }

  public void setup()
      throws Exception
  {
    if (top_prot == null)
    {
      Vector v = new Vector();
      top_prot = new SMAPProcessor();
      if (top_prot == null)
      {
        throw new Exception("couldn't create protocol stack");
      }
      top_prot.setUpProtocol(this);
      bottom_prot = new TCP_NIO();
      bottom_prot.enableDownThread(false);
      bottom_prot.enableUpThread(false);
      v.add(bottom_prot);
      v.add(new SMAPProtocol());
      v.add(new SMAPControlProtocol());
      v.add(top_prot);
      Protocol up_prot = null;
      Protocol prot;
      Protocol down_prot = null;
      for (int i = 0; i < v.size() - 1; i++)
      {
        prot = (Protocol) v.get(i);
        up_prot = (Protocol) v.get(i + 1);
        prot.setDownProtocol(down_prot);
        prot.setUpProtocol(up_prot);
        up_prot.setDownProtocol(prot);
        down_prot = prot;
      }
      prot = top_prot;
      while(prot !=null)
      {
         prot.init();
         prot.startDownHandler();
         prot.startUpHandler();
         prot =prot.getDownProtocol();
      }
    }
  }

  /**
   * Creates a new protocol given the protocol specification.
   * @param prot_spec The specification of the protocol. Same convention as for specifying a protocol stack.
   *                  An exception will be thrown if the class cannot be created. Example:
   *                  <pre>"VERIFY_SUSPECT(timeout=1500)"</pre> Note that no colons (:) have to be
   *                  specified
   * @return Protocol The newly created protocol
   * @exception Exception Will be thrown when the new protocol cannot be created
   */
  public Protocol createProtocol(String prot_spec)
      throws Exception
  {
    return null;
  }

  /**
   * Inserts an already created (and initialized) protocol into the protocol list. Sets the links
   * to the protocols above and below correctly and adjusts the linked list of protocols accordingly.
   * Note that this method may change the value of top_prot or bottom_prot.
   * @param prot The protocol to be inserted. Before insertion, a sanity check will ensure that none
   *             of the existing protocols have the same name as the new protocol.
   * @param position Where to place the protocol with respect to the neighbor_prot (ABOVE, BELOW)
   * @param neighbor_prot The name of the neighbor protocol. An exception will be thrown if this name
   *                      is not found
   * @exception Exception Will be thrown when the new protocol cannot be created, or inserted.
   */
  public void insertProtocol(Protocol prot, int position, String neighbor_prot)
      throws Exception
  {
    //  conf.insertProtocol(prot, position, neighbor_prot, this);
  }

  /**
   * Removes a protocol from the stack. Stops the protocol and readjusts the linked lists of
   * protocols.
   * @param prot_name The name of the protocol. Since all protocol names in a stack have to be unique
   *                  (otherwise the stack won't be created), the name refers to just 1 protocol.
   * @exception Exception Thrown if the protocol cannot be stopped correctly.
   */
  public void removeProtocol(String prot_name)
      throws Exception
  {
    //   conf.removeProtocol(prot_name);
  }

  /** Returns a given protocol or null if not found */
  public Protocol findProtocol(String name)
  {
    Protocol tmp = top_prot;
    String prot_name;
    while (tmp != null)
    {
      prot_name = tmp.getName();
      if (prot_name != null && prot_name.equals(name))
      {
        return tmp;
      }
      tmp = tmp.getDownProtocol();
    }
    return null;
  }

  public void destroy()
  {
    if (top_prot != null)
    {
      // conf.stopProtocolStack(top_prot);           // destroys msg queues and threads
      top_prot = null;
    }
  }

  /**
   * Start all layers. The {@link Protocol#start()} method is called in each protocol,
   * <em>from top to bottom</em>.
   * Each layer can perform some initialization, e.g. create a multicast socket
   */
  public void startStack()
      throws Exception
  {
    Object start_result = null;
    if (stopped == false)
    {
      return;
    }

    //  timer.start();

        if(start_promise == null)
            start_promise=new Promise();
        else
            start_promise.reset();

        down(new Event(Event.START));
        start_result=start_promise.getResult(0);
    if (start_result != null && start_result instanceof Throwable)
    {
      if (start_result instanceof Exception)
      {
        throw (Exception) start_result;
      }
      else
      {
        throw new Exception("failed starting stack: " + start_result);
      }

    }
    System.out.println("protocol stack-----启动 完成");
    stopped = false;
  }

  public void startUpHandler()
  {
    // DON'T REMOVE !!!!  Avoids a superfluous thread
  }

  public void startDownHandler()
  {
    // DON'T REMOVE !!!!  Avoids a superfluous thread
  }

  /**
   * Iterates through all the protocols <em>from top to bottom</em> and does the following:
   * <ol>
   * <li>Waits until all messages in the down queue have been flushed (ie., size is 0)
   * <li>Calls stop() on the protocol
   * </ol>
   */
  public void stopStack()
  {
//        if(timer != null) {
//            try {
//                timer.stop();
//            }
//            catch(Exception ex) {
//            }
//        }

    if (stopped)
    {
      return;
    }

        if(stop_promise == null)
            stop_promise=new Promise();
        else
            stop_promise.reset();

        down(new Event(Event.STOP));
        stop_promise.getResult(5000);
    stopped = true;
  }

  /**
   * Not needed anymore, just left in here for backwards compatibility with JBoss AS
   * @deprecated
   */
  public void flushEvents()
  {

  }

  public void stopInternal()
  {
    // do nothing, DON'T REMOVE !!!!
  }

  /*--------------------------- Transport interface ------------------------------*/

  public void send(Message msg)
      throws Exception
  {
    down(new Event(Event.MSG, msg));
  }

  public Object receive(long timeout)
      throws Exception
  {
    throw new Exception("ProtocolStack.receive(): not implemented !");
  }

  /*------------------------- End of  Transport interface ---------------------------*/





  /*--------------------------- Protocol functionality ------------------------------*/
  public String getName()
  {
    return "ProtocolStack";
  }

  public void up(Event evt)
  {
       System.out.println(getName()+"收到上行消息******************************");
        switch(evt.getType()) {
            case Event.START_OK:
                if(start_promise != null)
                    start_promise.setResult(evt.getArg());
                return;
            case Event.STOP_OK:
                if(stop_promise != null)
                    stop_promise.setResult(evt.getArg());
                return;
        }

//        if(channel != null)
//            channel.up(evt);
  }

  public void down(Event evt)
  {
    if (top_prot != null)
    {
      top_prot.receiveDownEvent(evt);
    }
    else
    {
      log.error("no down protocol available !");
    }
  }

  protected void receiveUpEvent(Event evt)
  {
    up(evt);
  }

  /** Override with null functionality: we don't need any threads to be started ! */
  public void startWork()
  {}

  /** Override with null functionality: we don't need any threads to be started ! */
  public void stopWork()
  {}

  /*----------------------- End of Protocol functionality ---------------------------*/

}
