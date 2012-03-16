package org.noneorone.pattern.builder;

/** 
 * Title: base<br> 
 * Description: Just for a test.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 6, 2011 8:58:53 PM <br> 
 * @author wangmeng
 */
public class Commission {

	public static void main(String[] args){
		
		NuWa nuWa = new NuWa();
		FrogPrinceBuilder frogPrinceBuilder = new FrogPrinceBuilder();
		SnowWhiteBuilder snowWhiteBuilder = new SnowWhiteBuilder();
		
		/**
		 * Build the specified product.
		 */
		nuWa.setPersonBuilder(frogPrinceBuilder);
		nuWa.constructPerson();
		Person person = nuWa.getPerson();
		System.out.println("Frog Prince: I am a " + person.getSex() + " who looks " + person.getLooks() + " and " + person.getFigure() + ".");

		nuWa.setPersonBuilder(snowWhiteBuilder);
		nuWa.constructPerson();
		person = nuWa.getPerson();
		System.out.println("Snow White: I am a " + person.getSex() + " who looks " + person.getLooks() + " and " + person.getFigure() + ".");
		
	}
}
