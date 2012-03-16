package org.noneorone.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sa {

	public static void main(String[] args) {
		String content = " sdfsdf                 dsfsfsd           sd";
//		content = content.replace("(^\\s*)/g", "");
		System.out.println(content);
		Pattern p = Pattern.compile("^\\s*\\n$"); // 正则表达式
		Matcher m = p.matcher(content); // 操作的字符串
		String s = m.replaceAll(""); //替换后的字符串
		System.out.println(s);
	}
}
