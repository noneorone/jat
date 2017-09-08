package org.noneorone.lang.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commission {

	public static void main(String[] args) {
		
		String letters = " hello   tom ,  how are          you   ?";
		
		/**½ØÈ¡ËùÓÐ¿Õ°××Ö·û**/
		Pattern pattern = Pattern.compile(RegExpConst.BLANK_CHAR);
		Matcher matcher = pattern.matcher(letters);
		System.out.println(letters);
		letters = matcher.replaceAll("");
		System.out.println(letters);
		
	}
}
