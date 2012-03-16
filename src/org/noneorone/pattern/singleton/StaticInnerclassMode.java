package org.noneorone.pattern.singleton;

/** 
 * Title: base<br> 
 * Description: Static Inner-class Mode<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 23, 2011 8:53:27 PM <br> 
 * @author wangmeng
 */
public class StaticInnerclassMode {

	private static class StaticInnerclassModeFactory{
		private static final StaticInnerclassMode mode = new StaticInnerclassMode();
	}
	
	public static StaticInnerclassMode getInstance(){
		return StaticInnerclassModeFactory.mode;
	}
	
	private StaticInnerclassMode(){}
}
