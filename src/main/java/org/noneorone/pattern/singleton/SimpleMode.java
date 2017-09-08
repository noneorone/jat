package org.noneorone.pattern.singleton;

/** 
 * Title: base<br> 
 * Description: Simple Mode<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 23, 2011 8:23:51 PM <br> 
 * @author wangmeng
 */
public class SimpleMode {

	private static final SimpleMode mode = new SimpleMode();
	
	private SimpleMode(){}
	
	public static SimpleMode getInstance(){
		return mode;
	}
}
