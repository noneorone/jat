package org.noneorone.lang.enumeration;

/** 
 * Title: base<br> 
 * Description: Test<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 8, 2011 2:44:47 PM <br> 
 * @author wangmeng
 */
public class Commission {

	@SuppressWarnings("static-access")
	public static void main(String[] args){
		BizAspect biz = new BizAspect();
		biz.setHighTechName("…Ã”√∏ﬂ∂À");
		biz.setThinType(ThinType.BI);
		System.out.println(biz.getHighTechName()+ ": " +biz.getThinType().valueOf(3).toString());
	}
	
}
