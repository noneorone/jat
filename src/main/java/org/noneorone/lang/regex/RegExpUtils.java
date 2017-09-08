package org.noneorone.lang.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtils {

	private RegExpUtils(){}
	
	/**
	 * To find and replace strings.
	 * @param original
	 * @param replace
	 * @return
	 */
	public static StringBuilder findAndReplace(StringBuilder original, String regex, String replacement){
		Pattern pattern = Pattern.compile(regex, Pattern.LITERAL);
		Matcher matcher = pattern.matcher(original);
		while(matcher.find()){
			original.replace(matcher.start(), matcher.end(), replacement);
		}
		return original;
	}
	
}
