package org.sunnysolong.pattern.builder;

/** 
 * Title: base<br> 
 * Description: Concrete Builder<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 6, 2011 8:43:04 PM <br> 
 * @author wangmeng
 */
public class FrogPrinceBuilder extends PersonBuilder{

	@Override
	public void buildFigure() {
		this.person.setFigure("strong");
	}

	@Override
	public void buildLooks() {
		this.person.setLooks("handsome");
	}

	@Override
	public void buildSex() {
		this.person.setSex("male");
	}

}
