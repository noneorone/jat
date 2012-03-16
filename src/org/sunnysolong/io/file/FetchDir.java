package org.sunnysolong.io.file;

import java.io.File;

/** 
 * Title: base<br> 
 * Description: Fetch Local Directories & Files.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 16, 2011 8:12:23 PM <br> 
 * @author wangmeng
 */
public class FetchDir {

	public static void fetchDir(String path){
		File fp = new File(path);
		File[] roots = fp.listFiles();
		if(null != roots){
			for(File root : roots){
				System.out.println(root.getPath());
				if(root.isDirectory()){
					File[] files = root.listFiles();
					if(null != files){
						for(File file : files){
							System.out.println(file.getPath());
							if(file.isDirectory()){
								//iterate handler
								fetchDir(file.getPath());
							}
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		/**Get local roots.*/
		File[] files = File.listRoots();
		for(File file : files){
			fetchDir(file.getPath());
		}
	}
}
