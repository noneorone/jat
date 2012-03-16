package org.sunnysolong.pattern.singleton;

/** 
 * Title: base<br> 
 * Description: Synchronized Mode<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 23, 2011 8:28:33 PM <br> 
 * @author wangmeng
 */
public class SynchronizedMode {

	private static SynchronizedMode mode = null;
	
	private SynchronizedMode(){}
	
	public static synchronized SynchronizedMode getInstance(){
		if(null == mode){
			mode = new SynchronizedMode();
		}
		return mode;
	}
}
