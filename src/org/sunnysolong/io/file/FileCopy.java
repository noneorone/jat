package org.sunnysolong.io.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
* Title: JavaTech<br>
* Description: File Handler Sample<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Jul 14, 2011 9:30:28 AM <br>
* @author wangmeng
*/
public class FileCopy {

	private FileCopy(){}

	/**
	 * Execute specified file.
	 * TMD,this can occur the error sometime like this: 
	 * @param file
	 */
	private static void open(String file){
		try {
			Runtime.getRuntime().exec("explorer " + file +"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Local copying.
	 * @param source
	 * @param destination
	 * @param open
	 */
	public static void localCopy(String source, String directory, boolean open){
		InputStream is = null;
		File src = null;
		String path = null;
		try {
			src = new File(source);
			is = new FileInputStream(src);
			System.out.println("file size: " + (Double.parseDouble(String.valueOf(src.length())) / 1024 / 1024) + "MB");
			path = directory + "\\" + source.substring(source.lastIndexOf('\\') + 1);
			writeBytesIntoFile(convertToBytes(is), path);
			if(open){
				System.out.println("Opening the file " + path + " ...");
				open(path);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Remote copying.
	 * @param source
	 * @param destination
	 * @param open
	 */
	private static void remoteCopy(String source, String directory, boolean open){
		URL url = null;
		URLConnection urlConn = null;
		String path = null;
		int contentLength = 0;
		try {
			url = new URL(source);
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.connect();
			contentLength = urlConn.getContentLength();
			System.out.println("file size: " + (Double.parseDouble(String.valueOf(contentLength)) / 1024 / 1024) + "MB");
			if(contentLength == -1){
				throw new Exception("TMD,the current net is a little busy, please try again later.");
			}
			path = directory + "\\" + source.substring(source.lastIndexOf('/') + 1);
			writeBytesIntoFile(convertToBytes(urlConn.getInputStream()), path);	
			if(open){
				System.out.println("Opening the file " + path + " ...");
				open(path);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Copy key entrance.
	 * @param source
	 * @param destination
	 * @param open
	 */
	public static void copy(String source, String directory, boolean open){
			if(source.startsWith("http://") || source.startsWith("https://")){
				System.out.println("Copying the remote source " + source + " to the directory " + directory + " ...");
				remoteCopy(source, directory, open);
				System.out.println("Copying ended ...");
			}else{
				System.out.println("Copying the local source " + source + " to the directory " + directory + " ...");
				localCopy(source, directory, open);
				System.out.println("Copying ended ...");
			}

	}

	/**
	 * Read input stream into a byte array and as a return value.
	 * @param is
	 * @return byte[]
	 */
	public static byte[] convertToBytes(InputStream is){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		try {
			while((len = is.read(b)) != -1){
				out.write(b, 0, len);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	
	/**
	 * Write bytes into specified file.
	 * @param b
	 * @param path
	 * @return File: the current file.
	 */
	public static File writeBytesIntoFile(byte[] b, String path){
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File(path);
			if(file.exists())
				file.delete();
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(b);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	
	/**
	 * Copy image.
	 * @param source
	 * @param directory
	 */
	public static void imageCopy(String source, String directory, boolean open){
		URL url = null;
		HttpURLConnection conn = null;
		String path = null;
		try {
			url = new URL(source);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(2000);
			System.out.println("image file length: " + conn.getContentLength());
			path = directory + "\\" + source.substring(source.lastIndexOf('/') + 1);
			writeBytesIntoFile(convertToBytes(conn.getInputStream()), path);
			if(open){
				open(path);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String source = "http://www.rubyist.net/~slagell/ruby/eval.txt";
		FileCopy.copy(source, System.getProperty("user.home"), true);
	}
	
	
}
