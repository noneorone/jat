package org.sunnysolong.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * Title: base<br> 
 * Description: Dynamic Generate HTMLs<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 15, 2011 11:18:59 AM <br> 
 * @author wangmeng
 */
public class JspToHtml {

	/**Replace the title and context of the HTML.*/
	private static String title = "Dyn-Title";
	private static String context = "Dyn-Context";

	public static boolean jspToHtmlFile(String filePath, String htmlFile) {

		String str = "";
		try {
			FileInputStream is = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String tempStr = "";
			while ((tempStr = br.readLine()) != null) {
				str = str + tempStr;
			}

			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			str = str.replaceAll("##title##", title);
			str = str.replaceAll("##context##", context);
			File file = new File(htmlFile);
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(str);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	public static void main(String[] args) {
		String url = "D:\\templete.html";
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date());
		String savePath = "D:\\" + date.replaceAll(" ", "-").replaceAll(":", "-") + ".html";

		jspToHtmlFile(url, savePath);
	}

}