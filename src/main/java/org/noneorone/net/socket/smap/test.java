package org.noneorone.net.socket.smap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class test
{
  public test()
  {
  }
  
  /**
   * Ð­ÒéÕ»Ä£Äâ
   * @param args
   */
  public static void main(String[] args)
  {
    try
    {
   //   ConnectionTableNIO ct = new ConnectionTableNIO(null,8077);
  //    ct.start();
     ProtocolStack st=new ProtocolStack();
     st.setup();
     st.startStack();

     // ct.start();
      Thread.currentThread().sleep(1000000000);
     st.stopStack();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

}
