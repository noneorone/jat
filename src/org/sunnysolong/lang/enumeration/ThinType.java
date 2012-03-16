package org.sunnysolong.lang.enumeration;

/** 
 * Title: base<br> 
 * Description: Enumeration,which is to be as a rule.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 8, 2011 2:41:44 PM <br> 
 * @author wangmeng
 */
public enum ThinType {

	/**
	 * To define some enumeration. 
	 */
	BI("商务智能",0), UI("人机交互",1), Unknow("未知",2);
	
	private String name;
	private Integer value;
	
	private ThinType(String name, Integer value){
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Override the toString method.
	 */
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * Get the specified value of the current object.
	 * @return
	 */
	public Integer getValue() {
		return this.value;
	}
	
	/**
	 * Get the value via the specified key.
	 * @param value
	 * @return
	 */
	public static ThinType valueOf(Integer value){
		ThinType[] ttValues = ThinType.values();
		for(ThinType tt : ttValues){
			if(tt.value == value){
				return tt;
			}
		}
		return Unknow;
	}
}
