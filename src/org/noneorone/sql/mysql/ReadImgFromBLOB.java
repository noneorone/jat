//package com.des;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Blob;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import com.mysql.jdbc.Statement;
//import com.sun.corba.se.spi.orbutil.fsm.Input;
//
//public class Ts {
//  public static void doGetAndShowStaffPic(String loginname, String dir) {     
//   Connection conn = null;    
//   ResultSet rs = null; 
//   Statement stms=null;
//   String mysqlUrl="jdbc:mysql://192.168.1.185:3306/smartmapdb";
//   String mysqlDriverName="com.mysql.jdbc.Driver";
//   String mysqlUser="radiate";
//   String mysqlPasswd="radiate_2010";
//  
//   
//	   try {
//		Class.forName(mysqlDriverName);
//	
//       conn=DriverManager.getConnection(mysqlUrl,mysqlUser,mysqlPasswd);   
//       stms = (Statement) conn.createStatement();
//       String sql = "select voucherlogo from zt_sh_voucher  where cityid=351";
//       rs = stms.executeQuery(sql);
//       rs.next();
//       Blob blob = rs.getBlob("voucherlogo");
//       FileOutputStream out = new FileOutputStream(new File("E:\\lijian\\my.bmp"));
//       InputStream in = blob.getBinaryStream();
//       int i;
//       while ((i = in.read()) != -1) {
//        out.write(i);
//       }
//       in.close();
//       out.close();
//	   } catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//      } 
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		doGetAndShowStaffPic("","f:\\lijian");
//	}
//
//}
package org.noneorone.sql.mysql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import com.sun.corba.se.spi.orbutil.fsm.Input;

public class ReadImgFromBLOB {
  public static void doGetAndShowStaffPic() {     
   Connection conn = null;    
   ResultSet rs = null; 
   Statement stms=null;
   FileOutputStream output=null;
   String mysqlUrl="jdbc:mysql://192.168.1.185:3306/smartmapdb";
   String mysqlDriverName="com.mysql.jdbc.Driver";
   String mysqlUser="radiate";
   String mysqlPasswd="radiate_2010";
   
    int i=0;
    int count = 0;
	   try {
		Class.forName(mysqlDriverName);
        conn=DriverManager.getConnection(mysqlUrl,mysqlUser,mysqlPasswd);   
        stms = (Statement) conn.createStatement();
        String sql = "select voucherlogo from zt_sh_voucher  where cityid=351";
        rs = stms.executeQuery(sql);
        rs.last();
        i = rs.getRow();
        System.out.println(i);
        rs.beforeFirst();
        while (rs.next()){
    	   Blob blob = rs.getBlob("voucherlogo");
    	   if (blob!=null) {
			   InputStream in = blob.getBinaryStream();
			   Thread.sleep(200);
			   String pa = "E:\\"+ Calendar.getInstance().getTimeInMillis() +".jpg";
	    	   output = new FileOutputStream(pa); 
	    	   byte[] rb = new byte[1024000];    
	           int ch = 0;    
	           while ((ch = in.read(rb)) != -1) {    
	            output.write(rb, 0, ch);                                             
	           }    
	           in.close();
	           output.close();
	           count ++;
		   }
    	  
	   }
        System.out.println(count+"----count");
     
       
	   } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
		e.printStackTrace();
	} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		e.printStackTrace();
	}
      } 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			doGetAndShowStaffPic();
	}

}
