package org.noneorone.pattern.builder;

/** 
 * Title: base<br> 
 * Description: Final Product<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 6, 2011 8:10:55 PM <br> 
 * @author wangmeng
 */
public class Person {

	@SuppressWarnings("unused")
	private String looks;
	@SuppressWarnings("unused")
	private String figure;
	@SuppressWarnings("unused")
	private String sex;
	
	public void setLooks(String looks){
		this.looks  = looks;
	}
	
	public void setFigure(String figure){
		this.figure = figure;
	}
	
	public void setSex(String sex){
		this.sex = sex;
	}
	
	public String getLooks(){
		return this.looks;
	}
	
	public String getFigure(){
		return this.figure;
	}
	
	public String getSex(){
		return this.sex;
	}
}
