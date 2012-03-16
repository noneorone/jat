package org.sunnysolong.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileRandomAccessKen {

	public static void main(String[] args){
		
		File file = new File("F:/tmp.txt");
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file,"rw");
			raf.setLength(1024);
			System.out.println(raf.readLine());
			raf.seek(10);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
