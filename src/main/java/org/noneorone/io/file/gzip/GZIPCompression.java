package org.noneorone.io.file.gzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
* Title: terminal<br>
* Description: GZIP compression and decompression<br>
* Copyright: Copyright (c) 2012 <br>
* Create DateTime: Feb 25, 2012 5:44:11 PM <br>
* @author wangmeng
*/
public class GZIPCompression {

	
	/**
	 * the compression of a specified file path.
	 * @param path
	 */
	@SuppressWarnings("unused")
	private static void gzip(String path){
		String suffix = path.substring(path.lastIndexOf("."), path.length());
		String target = path.substring(0, path.lastIndexOf(".")) +suffix + ".zip";
		File srcFile, targetFile = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		GZIPOutputStream gzipos = null;
		byte[] b = new byte[1024];
		try {
			srcFile = new File(path);
			targetFile = new File(target);
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(targetFile);
			gzipos = new GZIPOutputStream(fos);
			int len = 0;
			while((len = fis.read(b)) !=-1){
				gzipos.write(b, 0, len);
			}
			gzipos.close();
			fos.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * the decompression of a specified file path.
	 * @param path
	 */
	@SuppressWarnings("unused")
	private static void ungzip(String path){
		String suffix = path.substring(path.indexOf("."), path.lastIndexOf("."));
		String target = path.substring(0, path.indexOf(".")) +suffix;
		File srcFile, targetFile = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		GZIPInputStream gzipis = null;
		byte[] b = new byte[1024];
		try {
			srcFile = new File(path);
			targetFile = new File(target);
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(targetFile);
			gzipis = new GZIPInputStream(fis);
			int len = 0;
			while((len = gzipis.read(b)) !=-1){
				fos.write(b, 0, len);
			}
			fos.close();
			gzipis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * the main application entrance.
	 * test commission
	 * @param args
	 */
	public static void main(String[] args) {
//		gzip("D:\\hand.txt");
//		ungzip("D:\\hand.txt.zip");
	}
}
