package org.sunnysolong.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;

/**
* Title: JavaTech<br>
* Description: File Base Handler<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Apr 1st, 2011 5:55:55 PM <br>
* @author wangmeng
*/
public class FileBase {

	public static void main(String[] args){
		
		File file = new File("F:\\mars.log");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//method¢Ù: Write text into the specific file via FileWriter object.
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.append("hello,smart-map!");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//method¢Ú: Write text into the specific file via FileOutputStream object.
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			byte[] bytes = "\r\nhello,dear member!".getBytes();
			fos.write(bytes, 0, bytes.length);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//method¢Û: Write text into the specific file via FileWriter added with BufferedWriter object.
		FileWriter w = null;
		BufferedWriter bw = null;
		try {
			w = new FileWriter(file);
			bw = new BufferedWriter(w);
			bw.append("we need to think about the effect way to complete the work,just like an added buffered object.");
			bw.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//open the final file to look through the content in detail.
		try {
			Runtime.getRuntime().exec("explorer F:\\mars.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
