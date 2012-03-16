package org.noneorone.util.code;

import java.util.Vector;

public class Commission {

	public static void main(String[] args) {
		
		/**中文字符转拼音**/
		ChineseToLetter trans = new ChineseToLetter();
		Vector<String> name = trans.getLetter("三里小龙");
		System.out.println(trans.toString(name).toLowerCase());
		Vector<String> ans = trans.expand("三里小龙");
		System.out.println(ans);
		
		/**简转繁**/
	   FanToJian jf = new FanToJian();   
	   System.out.println(jf.conver("你怎么说话呢？",1));   
	}
}
