package org.noneorone.util.ocr;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.noneorone.io.image.ImageBinarizationHandler;

import com.asprise.util.ocr.OCR;

/**
* Title: JavaTech<br>
* Description: OCR Commission<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: July 18, 2011 3:39:14 PM <br>
* @author wangmeng
*/
public class OCRKen {

	/**
	 * Process
	 * @param src
	 * @return
	 */
	public static String process(String src){
		BufferedImage bi = null;
		String code = null;
		OCR ocr = null;
		try {
			bi = ImageIO.read(ImageBinarizationHandler.filter(src, "png"));
			ocr = new OCR();
			code = ocr.recognizeEverything(bi);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
	}
	
	public static void main(String[] args) {
//		String code = process("F:/identifyingCode.jpg");
//		String code = process("F:/cmf.bmp");
//		String code = process("D:\\Software\\OCR\\OcrCrack\\sample-images\\ocr.gif");
//		String code = process("F:\\validate_code\\img\\identifyingCode.jpg");
//		String code = process("F:\\validate_code\\img\\careers2-ad-header-so.png");
//		String code = process("F:\\validate_code\\img\\1232085272_ddvip_6338.png");
//		String code = process("E:\\Kaptcha.jpg");
		String code = process("D:\\test\\埋点log\\img\\20170927_162749_copy.png");
		System.out.println(code);
	}
}
