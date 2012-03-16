package org.noneorone.io.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
* Title: JavaTech<br>
* Description: Image Filter Handler<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Jul 15, 2011 10:26:44 AM <br>
* @author wangmeng
*/
public class ImageBinarizationHandler {

	private ImageBinarizationHandler(){}
	
	/**
	 * Image Filter
	 * @param pic
	 * @param suffix
	 * suffix only JPG£¬BMP£¬PNG
	 */
	public static File filter(String pic, String suffix){
		String[] format = {"jpg", "bmp", "png"};
		boolean exists = false;
		for(String f : format){
			if(f.equalsIgnoreCase(suffix)){
				exists = true;
				break;
			}
		}
		if(!exists){
			try {
				throw new Exception("Picture format is only the one of \"jpg, bmp, png\"!");
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				System.exit(0);
			}
		}
		FileInputStream fis = null;
		BufferedImage bi = null;  
		ImageBinarization cleaner = null;   
        String name = null;  
        File file = null;  
		try {
			fis = new FileInputStream(pic);
			bi = ImageIO.read(fis);
			cleaner = new ImageBinarization(bi);
	        cleaner.changeGrey();  
	        cleaner.getGrey();  
	        cleaner.getBrighten();  
	        name = pic.substring(0,pic.lastIndexOf("."));  
	        file = new File(name + "." + suffix);  
	        ImageIO.write(bi, suffix, file);  		
	        bi = cleaner.getProcessedImg();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
}
