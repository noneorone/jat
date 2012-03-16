package org.sunnysolong.pattern.proxy.staticMode;

/** 
 * Title: base<br> 
 * Description: It's demander, which maybe need something to be solved with someone's help.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 24, 2011 6:43:39 PM <br> 
 * @author wangmeng
 */
public class Demander {

	public static void main(String[] args){
		
		AbstractRole role = new ProxyRole();
		
		/**
		 * Coming up with a demand.
		 */
		System.out.println("I want to marry a girl and build a warm family with her.");
		
		/**
		 * Let agent do it.
		 */
		role.response();
	}
}
