package org.noneorone.pattern.factoryMethod;

/** 
 * Title: base<br> 
 * Description: Concrete Creator<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 10:54:43 AM <br> 
 * @author wangmeng
 */
public class BadPersonFactory implements PersonFactory{

	public Person producePerson() {
		return new BadPerson();
	}

}
