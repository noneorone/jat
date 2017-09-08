package org.noneorone.net;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/** 
 * Title: base<br> 
 * Description: <br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 1, 2011 5:38:20 PM <br> 
 * @author wangmeng
 */
public class GrabImage {
	
	public BufferedImage loadImageLocal(String imgName)     
	{     
	    try    
	    {     
	        return ImageIO.read(new File(imgName));     
	    } catch (IOException e)     
	    {     
	        System.out.println(e.getMessage());     
	    }     
	    return null;     
	}     
	    
	    
	/**   
	* 导入网络图片到缓冲区   
	*/    
	public BufferedImage loadImageUrl(String imgName)     
	{     
	    try    
	    {     
	        URL url = new URL(imgName);     
	        return ImageIO.read(url);     
	    } catch (IOException e)     
	    {     
	        System.out.println(e.getMessage());     
	    }     
	    return null;     
	}     
	    
	    
	    
	/**   
	* 生成新图片到本地   
	*/    
	public void writeImageLocal(String newImage, BufferedImage img)     
	{     
	    if (newImage != null && img != null)     
	    {     
	        try    
	        {     
	            File outputfile = new File(newImage);     
	            ImageIO.write(img, "gif", outputfile);     
	        } catch (IOException e)     
	        {     
	            System.out.println(e.getMessage());     
	        }     
	    }     
	}     
}
