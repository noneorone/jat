package org.noneorone.lang.string;

/**
 * Title: JavaTech<br>
 * Description: 字符串转16进制中文乱码<br>
 * Copyright: Copyright (c) 2011 <br>
 * Create DateTime: Dec 29, 2011 4:24:10 PM <br>
 * 
 * @author 王猛
 */
public class HexString {

	public static String string2HexString(String strText) throws Exception {
		char c;
		String strRet = "";
		int intAsc;
		String strHex;
		for (int i = 0; i < strText.length(); i++) {
			c = strText.charAt(i);
			intAsc = (int) c;
			if (intAsc > 128) {
				strHex = Integer.toHexString(intAsc);
				strRet += "\\u" + strHex;
			} else {
				strRet = strRet + c;
			}
		}
		return strRet;
	}

	public static void main(String[] args) {
		try {
			System.out.println(string2HexString("三里vs小龙"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
