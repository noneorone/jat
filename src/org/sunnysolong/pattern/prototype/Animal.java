package org.sunnysolong.pattern.prototype;

/** 
 * Title: base<br> 
 * Description: Prototype<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 8:34:01 PM <br> 
 * @author wangmeng
 */
public abstract class Animal implements Cloneable{

	String statement;

	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	public Object clone(){
		Object obj = null;
		try {
			obj = this.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
}
