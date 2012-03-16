package org.noneorone.test;

import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		System.out.println(formatDate("19 Dec 2009"));
	}
	
	private static String formatDate(String date){
		Map<String, String> months = new HashMap<String, String>();
		months.put("Jan", "1");
		months.put("Feb", "2");
		months.put("Mar", "3");
		months.put("Apr", "4");
		months.put("May", "5");
		months.put("Jun", "6");
		months.put("Jul", "7");
		months.put("Aug", "8");
		months.put("Sept", "9");
		months.put("Oct", "10");
		months.put("Nov", "11");
		months.put("Dec", "12");
		String day = date.substring(0, date.indexOf(' '));
		String month = date.substring(date.indexOf(' ') + 1, date.lastIndexOf(' '));
		String year = date.substring(date.lastIndexOf(' ') + 1, date.length());
		return year + "Äê" + months.get(month) + "ÔÂ" + day + "ÈÕ";
	}
}
