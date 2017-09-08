package org.noneorone.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/** 
 * Title: base<br> 
 * Description: Grab Data from URL resource.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 5:41:07 PM <br> 
 * @author wangmeng
 */
public class Grab {

	/**
	 * Read out stream and fill it into a specified file.
	 * @param is
	 * @param filePath
	 */
	private static void readerHandle(InputStream is, String filePath){
		BufferedReader br = null;
		BufferedWriter bw = null;
		File file = null;
		String content = "";
		try {
			file = new File(filePath);
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			while(null != (content = br.readLine())){
				bw.append(content);
			}
			bw.close();
			br.close();
			Runtime.getRuntime().exec("explorer " + filePath + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		final String TARGET_URL = "http://www.gdgajj.com/cx/wzss/wzss.do";
		URL url = null;
		HttpURLConnection connection = null;
		String linkParams = "";
		try {
			url = new URL(TARGET_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Connection","close");
			connection.setRequestProperty("Keep-Alive","close");
			connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; (R1 1.5); .NETSPX2; .NET CLR 2.0.50727)");
			connection.setRequestProperty("Cache-Control","no-cache");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			linkParams="method=%E6%9F%A5%E8%AF%A2&jc=%E7%B2%A4&hphm=B12U40&hpzl=02&lxdh=1051&image2.x=44&image2.y=10";
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + (linkParams.getBytes()).length );
			connection.setRequestProperty("Referer",TARGET_URL);

			/** Append Parameters */
	    	OutputStream output = connection.getOutputStream();
	    	output.write(linkParams.getBytes());
	    	output.flush();
	    	output.close();

	    	connection.connect();
	    	System.out.println(connection.getResponseCode());
			
			System.out.println("start ...");
			readerHandle(connection.getInputStream(),"C:\\grab.html");
			System.out.println("end ...");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
