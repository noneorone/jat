package org.noneorone.pattern.factoryMethod;

/** 
 * Title: base<br> 
 * Description: Test<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 11:22:13 AM <br> 
 * @author wangmeng
 */
public class Commission {

	public static void main(String[] args){
		
		GoodPersonFactory goodPersonFactory = new GoodPersonFactory();
		BadPersonFactory badPersonFactory = new BadPersonFactory();
		
		goodPersonFactory.producePerson().survive();
		badPersonFactory.producePerson().survive();
		
	}
}
