package org.sunnysolong.pattern.singleton;

/** 
 * Title: base<br> 
 * Description: Double-checked Locking Mode<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 23, 2011 8:33:01 PM <br> 
 * @author wangmeng
 */
public class DoubleCheckedLockingMode {

	/***
	 * #Attentions#: Be volatile modified write the variables are not and the reading and writing code before adjustment, 
	 * read the variables are not and lagging behind the reading and writing code adjusted.
	 */
	private static volatile DoubleCheckedLockingMode mode = null;
	
	private DoubleCheckedLockingMode(){}
	
	public static DoubleCheckedLockingMode getInstance(){
		if(null == mode){
			synchronized(DoubleCheckedLockingMode.class){
				if(null == mode){
					mode = new DoubleCheckedLockingMode();
				}
			}
		}
		return mode;
	}
}
