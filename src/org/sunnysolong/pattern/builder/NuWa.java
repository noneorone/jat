package org.sunnysolong.pattern.builder;

/** 
 * Title: base<br> 
 * Description: Director<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 6, 2011 8:54:02 PM <br> 
 * @author wangmeng
 */
public class NuWa {

	private PersonBuilder personBuilder;
	
	public void setPersonBuilder(PersonBuilder personBuilder){
		this.personBuilder = personBuilder;
	}
	
	public Person getPerson(){
		return personBuilder.getPerson();
	}
	
	public void constructPerson(){
		personBuilder.buildPerson();
		personBuilder.buildLooks();
		personBuilder.buildFigure();
		personBuilder.buildSex();
	}
	
}
