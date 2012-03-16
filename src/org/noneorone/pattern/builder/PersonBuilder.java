package org.noneorone.pattern.builder;

/** 
 * Title: base<br> 
 * Description: Abstract Builder<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 6, 2011 8:28:07 PM <br> 
 * @author wangmeng
 */
public abstract class PersonBuilder {
	protected Person person;
	
	public Person getPerson(){
		return this.person;
	}
	
	public void buildPerson(){
		person = new Person();
	}
	
	public abstract void buildLooks();
	
	public abstract void buildFigure();
	
	public abstract void buildSex();
}
