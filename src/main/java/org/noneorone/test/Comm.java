package org.noneorone.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comm {

	
	private static String lightKeyword(String text, String keyword){
		if(null != text){
			if(null == keyword){
				return text;
			}
			Pattern pattern = Pattern.compile("(?i)" + keyword);
			Matcher matcher = pattern.matcher(text);
			int index = text.toLowerCase().indexOf(keyword.toLowerCase());
			if(index != -1){
				keyword = text.substring(index, index + keyword.length());
			}
			return matcher.replaceAll("<span style='background-color:#FFFF96'>" + keyword + "</span>");
		}
		return "";
	}
	
	
	public static void main(String[] args) {

		String text = "测试普通写信";
		String keyword = "测";
		System.out.println(lightKeyword(text, keyword));
	
	}
	
}
