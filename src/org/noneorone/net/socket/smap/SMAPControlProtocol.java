package org.noneorone.net.socket.smap;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */


import java.net.InetAddress;
import java.util.Properties;
import java.util.Collection;

/**
 * Transport using NIO
 * @author Scott Marlow
 * @author Alex Fu
 * @author Bela Ban
 * @version $Id: TCP_NIO.java,v 1.7 2006/06/24 13:17:32 smarlownovell Exp $
 */
public class SMAPControlProtocol extends Protocol
{





   public void start() throws Exception {

       super.start();
   }

   public void retainAll(Collection members) {
    //  ct.retainAll(members);
   }

   public void stop() {

       super.stop();
   }

   public String getName() {
        return "TCP_NIO";
    }



   /** Setup the Protocol instance acording to the configuration string */
   public boolean setProperties(Properties props) {
       String str;

       str=props.getProperty("reader_threads");
       if(str != null) {
          m_reader_threads=Integer.parseInt(str);
          props.remove("reader_threads");
       }

       str=props.getProperty("writer_threads");
       if(str != null) {
          m_writer_threads=Integer.parseInt(str);
          props.remove("writer_threads");
       }

       str=props.getProperty("processor_threads");
       if(str != null) {
          m_processor_threads=Integer.parseInt(str);
          props.remove("processor_threads");
       }

      str=props.getProperty("processor_minThreads");
      if(str != null) {
         m_processor_minThreads=Integer.parseInt(str);
         props.remove("processor_minThreads");
      }

      str=props.getProperty("processor_maxThreads");
      if(str != null) {
         m_processor_maxThreads =Integer.parseInt(str);
         props.remove("processor_maxThreads");
      }

      str=props.getProperty("processor_queueSize");
      if(str != null) {
         m_processor_queueSize=Integer.parseInt(str);
         props.remove("processor_queueSize");
      }

      str=props.getProperty("processor_keepAliveTime");
      if(str != null) {
         m_processor_keepAliveTime=Integer.parseInt(str);
         props.remove("processor_keepAliveTime");
      }

      return super.setProperties(props);
   }

   private int m_reader_threads = 8;

   private int m_writer_threads = 8;

   private int m_processor_threads = 10;                    // PooledExecutor.createThreads()
   private int m_processor_minThreads = 10;                 // PooledExecutor.setMinimumPoolSize()
   private int m_processor_maxThreads = 10;                 // PooledExecutor.setMaxThreads()
   private int m_processor_queueSize=100;                   // Number of queued requests that can be pending waiting
                                                            // for a background thread to run the request.
   private int m_processor_keepAliveTime = -1;              // PooledExecutor.setKeepAliveTime( milliseconds);
                                                            // A negative value means to wait forever

}
