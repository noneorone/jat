package org.noneorone.io.file;

import java.io.BufferedInputStream;
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
public class FileCopyOriginal {

	private FileCopyOriginal(){}

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
	private static void localCopy(String source, String directory, boolean open){
		FileOutputStream fos = null;
		FileInputStream fis = null;
		File src = null;
		File dest = null;
		String path = null;
		byte[] b = null;
		try {
			src = new File(source);
			fis = new FileInputStream(src);
			b = new byte[Integer.parseInt(String.valueOf(src.length()))];
			fis.read(b, 0, b.length);
			fis.close();
			path = directory + "\\" + source.substring(source.lastIndexOf('\\') + 1);
			dest = new File(path);
			if(dest.exists()){
				dest.deleteOnExit();
			}
			dest.createNewFile();
			fos = new FileOutputStream(dest);
			fos.write(b, 0, b.length);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fos){
					fos.close();
				}
				if(null != fis){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		if(open){
			open(path);
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
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		File dest = null;
		String path = null;
		int contentLength = 0;
		int read = 0;
		int offset = 0;
		try {
			url = new URL(source);
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.connect();
			contentLength = urlConn.getContentLength();
			if(contentLength == -1){
				throw new Exception("NND,jing ran not a er jing zhi File!");
			}
			bis = new BufferedInputStream(urlConn.getInputStream()); 
			byte[] data = new byte[contentLength];
			while(offset < contentLength){
				read = bis.read(data, offset, data.length - offset);
				if(read == -1)
					break;
				offset += read;
			}
			bis.close();
			if(offset != contentLength){
				System.out.println("There're " + (contentLength - offset) + "bytes left.");
				System.exit(0);
			}
			path = directory + "\\" + source.substring(source.lastIndexOf('/') + 1);
			dest = new File(path);
			fos = new FileOutputStream(dest);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(open){
			open(path);
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
				remoteCopy(source, directory, open);
			}else{
				localCopy(source, directory, open);
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
	public static void imageCopy(String source, String directory){
		URL url = null;
		HttpURLConnection conn = null;
		String path = null;
		try {
			url = new URL(source);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(2000);
			path = directory + "\\" + source.substring(source.lastIndexOf('/') + 1, source.length());
			writeBytesIntoFile(convertToBytes(conn.getInputStream()), path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
