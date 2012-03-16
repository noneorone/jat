package org.sunnysolong.pattern.factoryMethod;

/** 
 * Title: base<br> 
 * Description: Concrete Product<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 11:10:23 AM <br> 
 * @author wangmeng
 */
public class GoodPerson extends Person{

	@Override
	void survive() {
		System.out.println("I'm a good person, but I will be dead one day in the future.");
	}

}
