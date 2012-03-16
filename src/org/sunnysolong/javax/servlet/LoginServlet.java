package org.sunnysolong.javax.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet{

	private final static String SOURCE = "http://192.168.1.136:8080/foshan/system/login.do";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		System.out.println("jsessionId: "+req.getSession().getAttribute("JSESSIONID").toString());
		URL url = null;
		HttpURLConnection conn = null;
		String params = null;
		try {
			url = new URL(SOURCE);
			//Set request property.
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Connection","close");
			conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; (R1 1.5); .NETSPX2; .NET CLR 2.0.50727)");
			conn.setRequestProperty("Cache-Control","no-cache");
			conn.setRequestProperty("Cookie", "JSESSIONID="+req.getSession().getAttribute("JSESSIONID").toString());
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			params = "userCode=" + req.getParameter("userCode") + "&" + "password=" + req.getParameter("password") + "&" + "code=" + req.getParameter("code");
			conn.setRequestProperty("Content-Length", "" + (params.getBytes()).length );
			conn.setRequestProperty("Referer",SOURCE);
			
			//Append parameters into the out stream.
			OutputStream os = conn.getOutputStream();
			os.write(params.getBytes());
			os.flush();
			os.close();
			
			//Open the connection.
			conn.connect();
			
			//Judge the response status code 
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
				PrintWriter out = resp.getWriter();
				String msg = streamToString(conn.getInputStream());
				//ResponseMessage is okay as returned.
				System.out.println("response message:" + conn.getResponseMessage());
				System.out.println("input stream string:" + msg);
				out.print(msg);
				out.flush();
				out.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp){
		this.doPost(req, resp);
	}
	
	/**
	 * Convert input stream into byte array.
	 * @param is
	 * @return
	 */
	@SuppressWarnings("unused")
	private byte[] streamToBytes(InputStream is){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		try {
			while((len = is.read()) != -1){
				out.write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	
	/**
	 * Convert input stream into string.
	 * @param is
	 * @return
	 */
	private String streamToString(InputStream is){
		String content = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String read = "";
		try {
			while(null != (read = reader.readLine())){
				content += read;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	

	
}
