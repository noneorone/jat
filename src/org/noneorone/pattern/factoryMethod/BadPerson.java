package org.noneorone.pattern.factoryMethod;

/** 
 * Title: base<br> 
 * Description: Concrete Product<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 10:56:21 AM <br> 
 * @author wangmeng
 */
public class BadPerson extends Person{

	@Override
	void survive() {
		System.out.println("Although I'm a bad person, I need food and clothing and shelter and transportation.");
	}
	
}
