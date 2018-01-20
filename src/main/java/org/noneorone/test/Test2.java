package org.noneorone.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int flag = 1;

		int a = 1 << 0;
		int b = 1 << 1;
		int c = 1 << 2;

		int flag1 = flag | a;
		int flag2 = flag | b;
		int flag3 = flag | c;

		// System.out.println("a: " + a);
		// System.out.println("flag1: " + flag1);
		// System.out.println("b: " + b);
		// System.out.println("flag2: " + flag2);
		// System.out.println("c: " + c);
		// System.out.println("flag3: " + flag3);
		//
		// int abc = a | b | c;
		// System.out.println("abc: " + abc);

		// System.out.println("a | b: " + (55 | 24)); //
		// System.out.println("a ^ b: " + (2 ^ 5)); // ^ 奇数和做加法、偶数和做减法
		// System.out.println("a & b: " + (1 & 5));

		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		// DateFormat df = DateFormat.getDateInstance();
		// try {
		//// Date date = format.parse("2017-12-11 08:48:30");
		// Date date = format.parse("08:48:30");
		// System.out.println(date.getTime());
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }

		// double d = 2.5d;
		// System.out.println(Math.round(d));

//		String contentDisposition = "attachment; filename=\"pycredit.apk\";";
//		String contentDisposition = "attachment; filename*=utf-8' 'pycredit.apk";
		String contentDisposition = "attachment; filename=\"pycredit.apk\"; filename*=utf-8' 'pycredit.apk";
		System.out.println(contentDisposition);
		String fileName = getFileName(contentDisposition);
		System.out.println(fileName);
	}

	private static String getFileName(String contentDisposition) {
		String [] disp = contentDisposition.split(";");
		for (String s : disp) {
			if (s.contains("filename")) {
				if (s.contains("*=")) {
					return s.substring(s.lastIndexOf("'") + 1);
				} else {
					return s.substring(s.indexOf("\"")+ 1, s.length() - 1);
				}
			}
		}
		return contentDisposition;
	}

}
