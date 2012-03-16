package org.sunnysolong.pattern.factoryMethod;

/** 
 * Title: base<br> 
 * Description: Concrete Creator<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 10:26:31 AM <br> 
 * @author wangmeng
 */
public class GoodPersonFactory implements PersonFactory{

	public Person producePerson() {
		return new GoodPerson();
	}
	
}
