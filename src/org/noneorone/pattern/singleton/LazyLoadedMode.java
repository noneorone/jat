package org.noneorone.pattern.singleton;

/** 
 * Title: base<br> 
 * Description: Lazy-loaded Mode<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 23, 2011 8:03:18 PM <br> 
 * @author wangmeng
 */
public class LazyLoadedMode {

	private static LazyLoadedMode mode = null;
	
	private LazyLoadedMode(){}
	
	public static LazyLoadedMode getInstance(){
		if(null == mode){
			mode = new LazyLoadedMode();
		}
		return mode;
	}
}
