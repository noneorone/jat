package org.noneorone.net.socket.smap;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import sun.rmi.runtime.Executor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;
import java.util.HashMap;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Map;

public class ConnectionTableNIO
    implements Runnable
{
  final HashMap conns = new HashMap(); // keys: Addresses (peer address), values: Connection

  //Receiver            receiver=null;
  boolean use_send_queues = true;
  InetAddress bind_addr = null;
  IpAddress local_addr = null; // bind_addr + port of srv_sock
  int srv_port = 7800;
  int recv_buf_size = 120000;
  int send_buf_size = 60000;
  final Vector conn_listeners = new Vector(); // listeners to be notified when a conn is established/torn down
  final Object recv_mutex = new Object(); // to serialize simultaneous access to receive() from multiple Connections

  Reaper reaper = null; // closes conns that have been idle for more than n secs
  long reaper_interval = 60000; // reap unused conns once a minute
  long conn_expire_time = 300000; // connections can be idle for 5 minutes before they are reaped
  int sock_conn_timeout = 1000; // max time in millis to wait for Socket.connect() to return
  ThreadGroup thread_group = null;
  protected final Log log = LogFactory.getLog(getClass());

  boolean use_reaper = false; // by default we don't reap idle conns
  static final int backlog = 20; // 20 conn requests are queued by ServerSocket (addtl will be discarded)
  ServerSocket srv_sock = null;
  boolean reuse_addr = false;

  InetAddress external_addr = null;
  int max_port = 0; // maximum port to bind to (if < srv_port, no limit)
  Thread acceptor = null;

  private ServerSocketChannel m_serverSocketChannel;
  private Selector m_acceptSelector;
  protected final static Log LOG = LogFactory.getLog(ConnectionTableNIO.class);

  private WriteHandler[] m_writeHandlers;
  private int m_nextWriteHandler = 0;
  private final Object m_lockNextWriteHandler = new Object();

  private ReadHandler[] m_readHandlers;
  private int m_nextReadHandler = 0;
  private final Object m_lockNextReadHandler = new Object();

  // int max_port = 0; // maximum port to bind to (if < srv_port, no limit)
  //InetAddress external_addr = null;
  private Protocol prot;
  private volatile boolean serverStopping = false;

  /**
   * @param srv_port
   * @throws Exception
   */
  public ConnectionTableNIO(Protocol prot, int srv_port)
      throws Exception
  {
    System.out.println("------------------------");
    this.srv_port = srv_port;
    this.prot = prot;

  }

  public void up(Event evt)
  {
    prot.up(evt);
  }

  public void down(Event evt)
  {
    // prot.down(evt);
  }

  /**
   * @param srv_port
   * @param reaper_interval
   * @param conn_expire_time
   * @throws Exception
   */
  public ConnectionTableNIO(int srv_port, long reaper_interval,
                            long conn_expire_time)
      throws Exception
  {
    this.srv_port = srv_port;
    this.reaper_interval = reaper_interval;
    this.conn_expire_time = conn_expire_time;

  }

  /**
   * Try to obtain correct Connection (or create one if not yet existent)
   */
  Connection getConnection(IpAddress dest)
      throws Exception
  {
    Connection conn;
    SocketChannel sock_ch;

    synchronized (conns)
    {
      conn = (Connection) conns.get(dest);
      if (conn == null)
      {
        InetSocketAddress destAddress = new InetSocketAddress( ( (IpAddress)
            dest).getIpAddress(),
            ( (IpAddress) dest).getPort());
        sock_ch = SocketChannel.open(destAddress);
        conn = new Connection(sock_ch, dest);

        //   conn.sendLocalAddress(local_addr);
        // This outbound connection is ready

        sock_ch.configureBlocking(false);

        try
        {
          if (LOG.isTraceEnabled())
          {
            LOG.trace("About to change new connection send buff size from " +
                      sock_ch.socket().getSendBufferSize() + " bytes");
          }
          sock_ch.socket().setSendBufferSize(send_buf_size);
          if (LOG.isTraceEnabled())
          {
            LOG.trace("Changed new connection send buff size to " +
                      sock_ch.socket().getSendBufferSize() + " bytes");
          }
        }
        catch (IllegalArgumentException ex)
        {
          if (log.isErrorEnabled())
          {
            log.error("exception setting send buffer size to " +
                      send_buf_size + " bytes: " + ex);
          }
        }
        try
        {
          if (LOG.isTraceEnabled())
          {
            LOG.trace("About to change new connection receive buff size from " +
                      sock_ch.socket().getReceiveBufferSize() + " bytes");
          }
          sock_ch.socket().setReceiveBufferSize(recv_buf_size);
          if (LOG.isTraceEnabled())
          {
            LOG.trace("Changed new connection receive buff size to " +
                      sock_ch.socket().getReceiveBufferSize() + " bytes");
          }
        }
        catch (IllegalArgumentException ex)
        {
          if (log.isErrorEnabled())
          {
            log.error("exception setting receive buffer size to " +
                      send_buf_size + " bytes: " + ex);
          }
        }

        int idx;
        synchronized (m_lockNextWriteHandler)
        {
          idx = m_nextWriteHandler = (m_nextWriteHandler + 1) %
              m_writeHandlers.length;
        }
        conn.setupWriteHandler(m_writeHandlers[idx]);

        // Put the new connection to the queue
        try
        {
          synchronized (m_lockNextReadHandler)
          {
            idx = m_nextReadHandler = (m_nextReadHandler + 1) %
                m_readHandlers.length;
          }
          m_readHandlers[idx].add(conn);

        }
        catch (InterruptedException e)
        {
          if (LOG.isWarnEnabled())
          {
            LOG.warn("Thread (" + Thread.currentThread().getName() +
                     ") was interrupted, closing connection", e);
            // What can we do? Remove it from table then.
          }
          conn.destroy();
          throw e;
        }

        // Add connection to table
        addConnection(dest, conn);

        // notifyConnectionOpened(dest);
        if (LOG.isInfoEnabled())
        {
          LOG.info("created socket to " + dest);
        }
      }
      return conn;
    }
  }

  void addConnection(IpAddress peer, Connection c)
  {
    conns.put(peer, c);
    if (reaper != null && !reaper.isRunning())
    {
      reaper.start();
    }
  }

  public final void start()
      throws Exception
  {
    init();
    srv_sock = createServerSocket(srv_port, max_port);

    if (external_addr != null)
    {
      local_addr = new IpAddress(external_addr, srv_sock.getLocalPort());
    }
    else if (bind_addr != null)
    {
      local_addr = new IpAddress(bind_addr, srv_sock.getLocalPort());
    }
    else
    {
      local_addr = new IpAddress(srv_sock.getLocalPort());

    }
    if (log.isInfoEnabled())
    {
      log.info("server socket created on " + local_addr);

    }
    thread_group = new ThreadGroup(new ThreadGroup("JGroups threads"),
                                   "ConnectionTableGroup");

    acceptor = new Thread(thread_group, this, "ConnectionTable.AcceptorThread");
    acceptor.setDaemon(true);
    acceptor.start();
    use_reaper = true;
    // start the connection reaper - will periodically remove unused connections
//    if (use_reaper && reaper == null)
//    {
//      reaper = new Reaper();
//      reaper.start();
//
//    }
  }

  protected void init()
      throws Exception
  {

    m_writeHandlers = WriteHandler.create(3);
    m_readHandlers = ReadHandler.create(3, this);
  }

  /**
   * Closes all open sockets, the server socket and all threads waiting for incoming messages
   */
  public void stop()
  {

    serverStopping = true;
    // Stop the main selector
    m_acceptSelector.wakeup();

    // Stop selector threads
    for (int i = 0; i < m_readHandlers.length; i++)
    {
      try
      {
        m_readHandlers[i].add(new Shutdown());
      }
      catch (InterruptedException e)
      {
        LOG.error("Thread (" + Thread.currentThread().getName() +
                  ") was interrupted, failed to shutdown selector", e);
      }
    }
    for (int i = 0; i < m_writeHandlers.length; i++)
    {

      //  m_writeHandlers[i].QUEUE.put(new Shutdown());
      m_writeHandlers[i].SELECTOR.wakeup();

    }

    // then close the connections
    synchronized (conns)
    {
      Iterator it = conns.values().iterator();
      while (it.hasNext())
      {
        Connection conn = (Connection) it.next();
        conn.destroy();
      }
      conns.clear();
    }

  }

  /**
   * Acceptor thread. Continuously accept new connections and assign readhandler/writehandler
   * to them.
   */
  public void run()
  {
    Connection conn;

    while (m_serverSocketChannel.isOpen() && !serverStopping)
    {
      int num;
      try
      {
        num = m_acceptSelector.select();
        LOG.warn("Select operation :" + num);

      }
      catch (IOException e)
      {
        if (LOG.isWarnEnabled())
        {
          LOG.warn("Select operation on listening socket failed", e);
        }
        continue; // Give up this time
      }

      if (num > 0)
      {
        Set readyKeys = m_acceptSelector.selectedKeys();
        for (Iterator i = readyKeys.iterator(); i.hasNext(); )
        {
          SelectionKey key = (SelectionKey) i.next();
          i.remove();
          // We only deal with new incoming connections

          ServerSocketChannel readyChannel = (ServerSocketChannel) key.channel();
          SocketChannel client_sock_ch;
          try
          {
            client_sock_ch = readyChannel.accept();
            client_sock_ch.configureBlocking(false);
          }
          catch (IOException e)
          {
            if (LOG.isWarnEnabled())
            {
              LOG.warn(
                  "Attempt to accept new connection from listening socket failed",
                  e);
              // Give up this connection
            }
            continue;
          }

          if (LOG.isInfoEnabled())
          {
            LOG.info("accepted connection, client_sock=" +
                     client_sock_ch.socket());

          }
          try
          {

            if (LOG.isTraceEnabled())
            {
              LOG.trace("About to change new connection send buff size from " +
                        client_sock_ch.socket().getSendBufferSize() + " bytes");
            }
            client_sock_ch.socket().setSendBufferSize(send_buf_size);
            if (LOG.isTraceEnabled())
            {
              LOG.trace("Changed new connection send buff size to " +
                        client_sock_ch.socket().getSendBufferSize() + " bytes");
            }
          }
          catch (IllegalArgumentException ex)
          {
            if (log.isErrorEnabled())
            {
              log.error("exception setting send buffer size to " +
                        send_buf_size + " bytes: ", ex);
            }
          }
          catch (SocketException e)
          {
            if (log.isErrorEnabled())
            {
              log.error("exception setting send buffer size to " +
                        send_buf_size + " bytes: ", e);
            }
          }

          try
          {
            if (LOG.isTraceEnabled())
            {
              LOG.trace(
                  "About to change new connection receive buff size from " +
                  client_sock_ch.socket().getReceiveBufferSize() + " bytes");
            }
            client_sock_ch.socket().setReceiveBufferSize(recv_buf_size);
            if (LOG.isTraceEnabled())
            {
              LOG.trace("Changed new connection receive buff size to " +
                        client_sock_ch.socket().getReceiveBufferSize() +
                        " bytes");
            }
          }
          catch (IllegalArgumentException ex)
          {
            if (log.isErrorEnabled())
            {
              log.error("exception setting receive buffer size to " +
                        send_buf_size + " bytes: ", ex);
            }
          }
          catch (SocketException e)
          {
            if (log.isErrorEnabled())
            {
              log.error("exception setting receive buffer size to " +
                        recv_buf_size + " bytes: ", e);
            }
          }

          conn = new Connection(client_sock_ch, null);
          addConnection(conn.getPeerAddress(), conn);

          int idx;
          synchronized (m_lockNextWriteHandler)
          {
            idx = m_nextWriteHandler = (m_nextWriteHandler + 1) %
                m_writeHandlers.length;
          }
          conn.setupWriteHandler(m_writeHandlers[idx]);

          try
          {
            synchronized (m_lockNextReadHandler)
            {
              idx = m_nextReadHandler = (m_nextReadHandler + 1) %
                  m_readHandlers.length;
            }
            m_readHandlers[idx].add(conn);

          }
          catch (InterruptedException e)
          {
            if (LOG.isWarnEnabled())
            {
              LOG.warn(
                  "Attempt to configure read handler for accepted connection failed",
                  e);
              // close connection
            }
            conn.destroy();
          }
        } // end of iteration
      } // end of selected key > 0
    } // end of thread

    if (m_serverSocketChannel.isOpen())
    {
      try
      {
        m_serverSocketChannel.close();
      }
      catch (Exception e)
      {
        log.error("exception closing server listening socket", e);
      }
    }
    if (LOG.isTraceEnabled())
    {
      LOG.trace("acceptor thread terminated");

    }
  }

  /**
   * Finds first available port starting at start_port and returns server socket. Sets srv_port
   */
  protected ServerSocket createServerSocket(int start_port, int end_port)
      throws
      Exception
  {
    this.m_acceptSelector = Selector.open();
    m_serverSocketChannel = ServerSocketChannel.open();
    m_serverSocketChannel.configureBlocking(false);
    while (true)
    {
      try
      {
        SocketAddress sockAddr;
        if (bind_addr == null)
        {
          sockAddr = new InetSocketAddress(start_port);
          m_serverSocketChannel.socket().bind(sockAddr);
        }
        else
        {
          sockAddr = new InetSocketAddress(bind_addr, start_port);
          m_serverSocketChannel.socket().bind(sockAddr, backlog);
        }
      }
      catch (BindException bind_ex)
      {
        if (start_port == end_port)
        {
          throw (BindException) ( (new BindException(
              "No available port to bind to")).initCause(bind_ex));
        }
        start_port++;
        continue;
      }
      catch (SocketException bind_ex)
      {
        if (start_port == end_port)
        {
          throw (BindException) ( (new BindException(
              "No available port to bind to")).initCause(bind_ex));
        }
        start_port++;
        continue;
      }
      catch (IOException io_ex)
      {
        if (LOG.isErrorEnabled())
        {
          LOG.error("Attempt to bind serversocket failed, port=" + start_port +
                    ", bind addr=" + bind_addr, io_ex);
        }
        throw io_ex;
      }
      srv_port = start_port;
      break;
    }
    m_serverSocketChannel.register(this.m_acceptSelector,
                                   SelectionKey.OP_ACCEPT);
    return m_serverSocketChannel.socket();
  }

  protected void runRequest(IpAddress addr, ByteBuffer buf)
      throws
      InterruptedException
  {
    //    m_requestProcessors.execute(new ExecuteTask(addr, buf));
  }

  // Represents shutdown
  private static class Shutdown
  {
  }

  // ReadHandler has selector to deal with read, it runs in seperated thread
  private static class ReadHandler
      implements Runnable
  {
    private final Selector SELECTOR = initHandler();
    private final LinkedQueue QUEUE = new LinkedQueue();
    private final ConnectionTableNIO connectTable;

    ReadHandler(ConnectionTableNIO ct)
    {
      connectTable = ct;
    }

    public Selector initHandler()
    {
      // Open the selector
      try
      {
        return Selector.open();
      }
      catch (IOException e)
      {
        if (LOG.isErrorEnabled())
        {
          LOG.error(e);
        }
        throw new IllegalStateException(e.getMessage());
      }

    }

    /**
     * create instances of ReadHandler threads for receiving data.
     *
     * @param workerThreads is the number of threads to create.
     */
    private static ReadHandler[] create(int workerThreads,
                                        ConnectionTableNIO ct)
    {
      ReadHandler[] handlers = new ReadHandler[workerThreads];
      for (int looper = 0; looper < workerThreads; looper++)
      {
        handlers[looper] = new ReadHandler(ct);

        Thread thread = new Thread(handlers[looper], "nioReadHandlerThread");
        thread.setDaemon(true);
        thread.start();
      }
      return handlers;
    }

    private void add(Object conn)
        throws InterruptedException
    {
      QUEUE.insert(conn);
      wakeup();
    }

    private void wakeup()
    {
      SELECTOR.wakeup();
    }

    public void run()
    {
      while (true)
      { // m_s can be closed by the management thread
        int events;
        try
        {
          events = SELECTOR.select();
        }
        catch (IOException e)
        {
          if (LOG.isWarnEnabled())
          {
            LOG.warn("Select operation on socket failed", e);
          }
          continue; // Give up this time
        }
        catch (ClosedSelectorException e)
        {
          if (LOG.isWarnEnabled())
          {
            LOG.warn("Select operation on socket failed", e);
          }
          return; // Selector gets closed, thread stops
        }

        if (events > 0)
        { // there are read-ready channels
          Set readyKeys = SELECTOR.selectedKeys();
          for (Iterator i = readyKeys.iterator(); i.hasNext(); )
          {
            SelectionKey key = (SelectionKey) i.next();
            i.remove();
            // Do partial read and handle call back
            Connection conn = (Connection) key.attachment();
            try
            {
              if (conn.getSocketChannel().isOpen())
              {
                readOnce(conn);
              }
              else
              { // socket connection is already closed, clean up connection state
                conn.closed();
              }
            }
            catch (IOException e)
            {
              if (LOG.isWarnEnabled())
              {
                LOG.warn("Read operation on socket failed", e);
                // The connection must be bad, cancel the key, close socket, then
                // remove it from table!
              }
              key.cancel();
              conn.destroy();
              conn.closed();
            }
          }
        }

        // Now we look at the connection queue to get any new connections added
        Object o;
        // try {
        try
        {
          o = QUEUE.poll(0); // get a connection
          if (null == o)
          {
            continue;
          }

        }
        catch (InterruptedException ex)
        {
          o = null;
          ex.printStackTrace();
        }
        //   }
//        catch (InterruptedException e) {
//          if (LOG.isInfoEnabled()) {
//            LOG.info("Thread (" + Thread.currentThread().getName() +
//                     ") was interrupted while polling queue", e);
//            // We must give up
//          }
        //     continue;
        //   }

        if (o instanceof Shutdown)
        { // shutdown command?
          try
          {
            SELECTOR.close();
          }
          catch (IOException e)
          {
            if (LOG.isInfoEnabled())
            {
              LOG.info("Read selector close operation failed", e);
            }
          }
          return; // stop reading
        }
        Connection conn = (Connection) o; // must be a new connection
        SocketChannel sc = conn.getSocketChannel();
        try
        {
          sc.register(SELECTOR, SelectionKey.OP_READ, conn);
        }
        catch (ClosedChannelException e)
        {
          if (LOG.isInfoEnabled())
          {
            LOG.info(
                "Socket channel was closed while we were trying to register it to selector",
                e);
            // Channel becomes bad. The connection must be bad,
            // close socket, then remove it from table!
          }
          conn.destroy();
          conn.closed();
        }
      } // end of the while true loop
    }

    private void readOnce(Connection conn)
        throws IOException
    {
      ConnectionReadState readState = conn.getReadState();
      if (!readState.isHeadFinished())
      { // a brand new message coming or header is not completed
        // Begin or continue to read header
        int size = readHeader(conn);
        if (0 == size)
        { // header is not completed
          return;
        }
      }
      // Begin or continue to read body
      if (readBody(conn) > 0)
      { // not finish yet
        return;
      }
      IpAddress src_addr = conn.getPeerAddress();
      ByteBuffer body_buf = readState.getReadBodyBuffer();
      ByteBuffer header_buf = readState.getReadHeadBuffer();
      header_buf.position(conn.HEADER_SIZE);
      header_buf.flip();
      int len = header_buf.capacity() + body_buf.capacity();
      byte[] msg_buf = new byte[len];
      header_buf.get(msg_buf, 0, conn.HEADER_SIZE);
      body_buf.get(msg_buf, conn.HEADER_SIZE, body_buf.capacity());
      Message msg = new Message(connectTable.local_addr, src_addr, msg_buf);
      Event evt = new Event(Event.UP_REQUEST, msg);
      connectTable.up(evt);
      // Clear status
      readState.bodyFinished();
      // Assign worker thread to execute call back

    }

    private int readHeader(Connection conn)
        throws IOException
    {
      ConnectionReadState readState = conn.getReadState();
      ByteBuffer headBuf = readState.getReadHeadBuffer();

      SocketChannel sc = conn.getSocketChannel();
      while (headBuf.remaining() > 0)
      {
        int num = sc.read(headBuf);
        if ( -1 == num)
        { // EOS
          throw new IOException("Peer closed socket");
        }
        if (0 == num)
        { // no more data
          return 0;
        }
      }
      // OK, now we get the whole header, change the status and return message size
      return readState.headFinished();
    }

    private int readBody(Connection conn)
        throws IOException
    {
      ByteBuffer bodyBuf = conn.getReadState().getReadBodyBuffer();

      SocketChannel sc = conn.getSocketChannel();
      while (bodyBuf.remaining() > 0)
      {
        int num = sc.read(bodyBuf);
        if ( -1 == num)
        { // EOS
          throw new IOException(
              "Couldn't read from socket as peer closed the socket");
        }
        if (0 == num)
        { // no more data
          return bodyBuf.remaining();
        }
      }
      // OK, we finished reading the whole message! Flip it (not necessary though)
      bodyBuf.flip();
      return 0;
    }
  }

  private class ExecuteTask
      implements Runnable
  {
    IpAddress m_addr = null;
    ByteBuffer m_buf = null;

    public ExecuteTask(IpAddress addr, ByteBuffer buf)
    {
      m_addr = addr;
      m_buf = buf;
    }

    public void run()
    {
      // receive(m_addr, m_buf.array(), m_buf.arrayOffset(), m_buf.limit());
    }
  }

  private class ConnectionReadState
  {
    private final Connection m_conn;

    // Status for receiving message
    private boolean m_headFinished = false;
    private ByteBuffer m_readBodyBuf = null;
    private final ByteBuffer m_readHeadBuf = ByteBuffer.allocate(Connection.
        HEADER_SIZE);

    public ConnectionReadState(Connection conn)
    {
      m_conn = conn;
    }

    ByteBuffer getReadBodyBuffer()
    {
      return m_readBodyBuf;
    }

    ByteBuffer getReadHeadBuffer()
    {
      return m_readHeadBuf;
    }

    void bodyFinished()
    {
      m_headFinished = false;
      m_readHeadBuf.clear();
      m_readBodyBuf = null;
      m_conn.updateLastAccessed();
    }

    /**
     * Status change for finishing reading the message header (data already in buffer)
     *
     * @return message size
     */
    int headFinished()
    {
      m_headFinished = true;
      m_readHeadBuf.flip();
      int messageSize = m_readHeadBuf.getInt();
      m_readBodyBuf = ByteBuffer.allocate(messageSize - Connection.HEADER_SIZE);
      m_conn.updateLastAccessed();
      return messageSize;

    }

    boolean isHeadFinished()
    {
      return m_headFinished;
    }
  }

  class Connection
  {
    Socket sock = null; // socket to/from peer (result of srv_sock.accept() or new Socket())
    String sock_addr = null; // used for Thread.getName()
    DataOutputStream out = null; // for sending messages
    DataInputStream in = null; // for receiving messages
    Thread receiverThread = null; // thread for receiving messages
    IpAddress peer_addr = null; // address of the 'other end' of the connection
    final Object send_mutex = new Object(); // serialize sends
    long last_access = System.currentTimeMillis(); // last time a message was sent or received

    private SocketChannel sock_ch = null;
    private WriteHandler m_writeHandler;
    private SelectorWriteHandler m_selectorWriteHandler;
    private final ConnectionReadState m_readState;

    private static final int HEADER_SIZE = 12;
    final ByteBuffer headerBuffer = ByteBuffer.allocate(HEADER_SIZE);

    Connection(SocketChannel s, IpAddress peer_addr)
    {

      sock_ch = s;
      this.sock = s.socket();
      this.peer_addr = peer_addr;
      m_readState = new ConnectionReadState(this);
    }

    void updateLastAccessed()
    {
      last_access = System.currentTimeMillis();
    }

    private ConnectionReadState getReadState()
    {
      return m_readState;
    }

    private void setupWriteHandler(WriteHandler hdlr)
    {
      m_writeHandler = hdlr;
      m_selectorWriteHandler = hdlr.add(sock_ch);
    }

    void destroy()
    {
      closeSocket();
    }

    void doSend(byte[] buffie, int offset, int length)
        throws Exception
    {
      FutureResult result = new FutureResult();
      m_writeHandler.write(sock_ch, ByteBuffer.wrap(buffie, offset, length),
                           result, m_selectorWriteHandler);
      Exception ex = result.getException();
      if (ex != null)
      {
        if (LOG.isErrorEnabled())
        {
          LOG.error("failed sending message", ex);
        }
        if (ex.getCause() instanceof IOException)
        {
          throw (IOException) ex.getCause();
        }
        throw ex;
      }
      result.get();
    }

    SocketChannel getSocketChannel()
    {
      return sock_ch;
    }

    void closeSocket()
    {

      if (sock_ch != null)
      {
        try
        {
          if (sock_ch.isConnected() && sock_ch.isOpen())
          {
            sock_ch.close();
          }
        }
        catch (Exception e)
        {
          log.error("error closing socket connection", e);
        }
        sock_ch = null;
      }
    }

    IpAddress getPeerAddress()
    {
      return peer_addr;
    }

    void closed()
    {
      IpAddress peerAddr = getPeerAddress();
      synchronized (conns)
      {
        conns.remove(peerAddr);
      }
      // notifyConnectionClosed(peerAddr);
    }
  }

  /**
   * Handle writing to non-blocking NIO connection.
   */
  private static class WriteHandler
      implements Runnable
  {
    // Create a queue for write requests
    private final LinkedQueue QUEUE = new LinkedQueue();

    private final Selector SELECTOR = initSelector();
    private int m_pendingChannels; // count of the number of channels that have pending writes

    // allocate and reuse the header for all buffer write operations
    private ByteBuffer m_headerBuffer = ByteBuffer.allocate(Connection.
        HEADER_SIZE);

    Selector initSelector()
    {
      try
      {
        return SelectorProvider.provider().openSelector();
      }
      catch (IOException e)
      {
        if (LOG.isErrorEnabled())
        {
          LOG.error(e);
        }
        throw new IllegalStateException(e.getMessage());
      }
    }

    /**
     * create instances of WriteHandler threads for sending data.
     *
     * @param workerThreads is the number of threads to create.
     */
    private static WriteHandler[] create(int workerThreads)
    {
      WriteHandler[] handlers = new WriteHandler[workerThreads];
      for (int looper = 0; looper < workerThreads; looper++)
      {
        handlers[looper] = new WriteHandler();

        Thread thread = new Thread(handlers[looper], "nioWriteHandlerThread");
        thread.setDaemon(true);
        thread.start();
      }
      return handlers;
    }

    /**
     * Add a new channel to be handled.
     *
     * @param channel
     */
    private SelectorWriteHandler add(SocketChannel channel)
    {
      return new SelectorWriteHandler(channel, SELECTOR, m_headerBuffer);
    }

    private void write(SocketChannel channel, ByteBuffer buffer,
                       FutureResult notification, SelectorWriteHandler hdlr)
        throws
        InterruptedException
    {
      QUEUE.insert(new WriteRequest(channel, buffer, notification, hdlr));
    }

    private void close(SelectorWriteHandler entry)
    {
      entry.cancel();
    }

    private void handleChannelError(SelectorWriteHandler entry, Throwable error)
    {

      do
      {
        if (error != null)
        {
          entry.notifyError(error);
        }
      }
      while (entry.next());
      close(entry);
    }

    // process the write operation
    private void processWrite(Selector selector)
    {
      Set keys = selector.selectedKeys();
      Object arr[] = keys.toArray();
      for (int looper = 0; looper < arr.length; looper++)
      {
        SelectionKey key = (SelectionKey) arr[looper];
        SelectorWriteHandler entry = (SelectorWriteHandler) key.attachment();
        boolean needToDecrementPendingChannels = false;
        try
        {
          if (0 == entry.write())
          { // write the buffer and if the remaining bytes is zero,
            // notify the caller of number of bytes written.
            entry.notifyObject(new Integer(entry.getBytesWritten()));
            // switch to next write buffer or clear interest bit on socket channel.
            if (!entry.next())
            {
              needToDecrementPendingChannels = true;
            }
          }

        }
        catch (IOException e)
        {
          needToDecrementPendingChannels = true;
          // connection must of closed
          handleChannelError(entry, e);
        }
        finally
        {
          if (needToDecrementPendingChannels)
          {
            m_pendingChannels--;
          }
        }
      }
      keys.clear();
    }

    public void run()
    {
      while (SELECTOR.isOpen())
      {
        //   try {
        WriteRequest queueEntry;
        Object o;

        // When there are no more commands in the Queue, we will hit the blocking code after this loop.
        try
        {
          while (null != (o = QUEUE.poll(0)))
          {
            if (o instanceof Shutdown)
            { // Stop the thread
              try
              {
                SELECTOR.close();
              }
              catch (IOException e)
              {
                if (LOG.isInfoEnabled())
                {
                  LOG.info("Write selector close operation failed", e);
                }
              }
              return;
            }
            queueEntry = (WriteRequest) o;

            if (queueEntry.getHandler().add(queueEntry))
            {

              m_pendingChannels++;
            }

            try
            {
              // process any connections ready to be written to.
              if (SELECTOR.selectNow() > 0)
              {
                processWrite(SELECTOR);
              }
            }
            catch (IOException e)
            { // need to understand what causes this error so we can handle it properly
              if (LOG.isErrorEnabled())
              {
                LOG.error("SelectNow operation on write selector failed, didn't expect this to occur, please report this",
                          e);
              }
              return; // if select fails, give up so we don't go into a busy loop.
            }
          }
        }
        catch (InterruptedException ex)
        {
        }

        // if there isn't any pending work to do, block on queue to get next request.
        if (m_pendingChannels == 0)
        {
          try
          {
            o = QUEUE.take();
          }
          catch (InterruptedException ex1)
          {
            o = null;
            ex1.printStackTrace();
            continue;
          }
          if (o instanceof Shutdown)
          { // Stop the thread
            try
            {
              SELECTOR.close();
            }
            catch (IOException e)
            {
              if (LOG.isInfoEnabled())
              {
                LOG.info("Write selector close operation failed", e);
              }
            }
            return;
          }
          queueEntry = (WriteRequest) o;
          if (queueEntry.getHandler().add(queueEntry))
          {
            m_pendingChannels++;
          }
        }
        // otherwise do a blocking wait select operation.
        else
        {
          try
          {
            if ( (SELECTOR.select()) > 0)
            {
              processWrite(SELECTOR);
            }
          }
          catch (IOException e)
          { // need to understand what causes this error
            if (LOG.isErrorEnabled())
            {
              LOG.error("Failure while writing to socket", e);
            }
          }
        }
        //  }
//        catch (InterruptedException e) {
//          if (LOG.isErrorEnabled()) {
//            LOG.error("Thread (" + Thread.currentThread().getName() +
//                      ") was interrupted", e);
//          }
//        }
//        catch (Throwable e) { // We are a daemon thread so we shouldn't prevent the process from terminating if
//          // the controlling thread decides that should happen.
//          if (LOG.isErrorEnabled()) {
//            LOG.error("Thread (" + Thread.currentThread().getName() +
//                      ") caught Throwable", e);
//          }
//        }
      }
    }
  }

  // Wrapper class for passing Write requests.  There will be an instance of this class for each socketChannel
  // mapped to a Selector.
  public static class SelectorWriteHandler
  {

    private final LinkedList m_writeRequests = new LinkedList(); // Collection of writeRequests
    private boolean m_headerSent = false;
    private SocketChannel m_channel;
    private SelectionKey m_key;
    private Selector m_selector;
    private int m_bytesWritten = 0;
    private boolean m_enabled = false;
    private ByteBuffer m_headerBuffer;

    SelectorWriteHandler(SocketChannel channel, Selector selector,
                         ByteBuffer headerBuffer)
    {
      m_channel = channel;
      m_selector = selector;
      m_headerBuffer = headerBuffer;
    }

    private void register(Selector selector, SocketChannel channel)
        throws
        ClosedChannelException
    {
      // register the channel but don't enable OP_WRITE until we have a write request.
      m_key = channel.register(selector, 0, this);
    }

    // return true if selection key is enabled when it wasn't previous to call.
    private boolean enable()
    {
      boolean rc = false;

      try
      {
        if (m_key == null)
        { // register the socket on first access,
          // we are the only thread using this variable, so no sync needed.
          register(m_selector, m_channel);
        }
      }
      catch (ClosedChannelException e)
      {
        return rc;
      }

      if (!m_enabled)
      {
        rc = true;
        try
        {
          m_key.interestOps(SelectionKey.OP_WRITE);
        }
        catch (CancelledKeyException e)
        { // channel must of closed
          return false;
        }
        m_enabled = true;
      }
      return rc;
    }

    private void disable()
    {
      if (m_enabled)
      {
        try
        {
          m_key.interestOps(0); // pass zero which means that we are not interested in being
          // notified of anything for this channel.
        }
        catch (CancelledKeyException eat)
        { // we probably don't need to throw this exception (if they try to write
          // again, we will then throw an exception).
        }
        m_enabled = false;
      }
    }

    private void cancel()
    {
      m_key.cancel();
    }

    boolean add(WriteRequest entry)
    {
      m_writeRequests.add(entry);
      return enable();
    }

    WriteRequest getCurrentRequest()
    {
      return (WriteRequest) m_writeRequests.getFirst();
    }

    SocketChannel getChannel()
    {
      return m_channel;
    }

    ByteBuffer getBuffer()
    {
      return getCurrentRequest().getBuffer();
    }

    FutureResult getCallback()
    {
      return getCurrentRequest().getCallback();
    }

    int getBytesWritten()
    {
      return m_bytesWritten;
    }

    void notifyError(Throwable error)
    {
      if (getCallback() != null)
      {
        getCallback().setException(error);
      }
    }

    void notifyObject(Object result)
    {
      if (getCallback() != null)
      {
        getCallback().set(result);
      }
    }

    boolean next()
    {
      m_headerSent = false;
      m_bytesWritten = 0;

      m_writeRequests.removeFirst(); // remove current entry
      boolean rc = !m_writeRequests.isEmpty();
      if (!rc)
      { // disable select for this channel if no more entries
        disable();
      }
      return rc;
    }

    int write()
        throws IOException
    {

      if (!m_headerSent)
      {
        m_headerSent = true;
        m_headerBuffer.clear();
        m_headerBuffer.putInt(getBuffer().remaining());
        m_headerBuffer.flip();
        do
        {
          getChannel().write(m_headerBuffer);
        } // we should be able to handle writing the header in one action but just in case, just do a busy loop
        while (m_headerBuffer.remaining() > 0);

      }

      m_bytesWritten += (getChannel().write(getBuffer()));

      return getBuffer().remaining();
    }

  }

  public static class WriteRequest
  {
    private final SocketChannel m_channel;
    private final ByteBuffer m_buffer;
    private final FutureResult m_callback;
    private final SelectorWriteHandler m_hdlr;

    WriteRequest(SocketChannel channel, ByteBuffer buffer,
                 FutureResult callback, SelectorWriteHandler hdlr)
    {
      m_channel = channel;
      m_buffer = buffer;
      m_callback = callback;
      m_hdlr = hdlr;
    }

    SelectorWriteHandler getHandler()
    {
      return m_hdlr;
    }

    SocketChannel getChannel()
    {
      return m_channel;
    }

    ByteBuffer getBuffer()
    {
      return m_buffer;
    }

    FutureResult getCallback()
    {
      return m_callback;
    }

  }

  class Reaper
      implements Runnable
  {
    Thread t = null;

    Reaper()
    {
      ;
    }

    public void start()
    {

      if (t != null && !t.isAlive())
      {
        t = null;
      }
      if (t == null)
      {
        //RKU 7.4.2003, put in threadgroup
        t = new Thread(thread_group, this, "ConnectionTable.ReaperThread");
        //  t.setDaemon(true); // will allow us to terminate if all remaining threads are daemons
        t.start();

      }
    }

    public void stop()
    {
      if (t != null)
      {
        t = null;
      }
    }

    public boolean isRunning()
    {
      return t != null;
    }

    public void run()
    {
      Connection value;
      Map.Entry entry;
      long curr_time;

      if (log.isInfoEnabled())
      {
        log.info("connection reaper thread was started. Number of connections=" +
                 conns.size() + ", reaper_interval=" + reaper_interval +
                 ", conn_expire_time=" +
                 conn_expire_time);

      }
      while (conns.size() > 0 && t != null && t.equals(Thread.currentThread()))
      {

        // Util.sleep(reaper_interval);
        synchronized (conns)
        {
          curr_time = System.currentTimeMillis();
          for (Iterator it = conns.entrySet().iterator(); it.hasNext(); )
          {
            entry = (Map.Entry) it.next();
            value = (Connection) entry.getValue();
            if (log.isInfoEnabled())
            {
//              log.info("connection is " +
//                       ( (curr_time - value.last_access) / 1000) +
//                       " seconds old (curr-time=" +
//                       curr_time + ", last_access=" + value.last_access + ')');
            }
            if (value.last_access + conn_expire_time < curr_time)
            {
              if (log.isInfoEnabled())
              {
                log.info("connection " + value +
                         " has been idle for too long (conn_expire_time=" +
                         conn_expire_time +
                         "), will be removed");
              }
              value.destroy();
              it.remove();
            }
          }
        }
      }
      if (log.isInfoEnabled())
      {
        log.info("reaper terminated");
      }
      t = null;
    }
  }

}
