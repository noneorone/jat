package org.noneorone.pattern.proxy.staticMode;

/** 
 * Title: base<br> 
 * Description: It's a real role.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 24, 2011 6:31:58 PM <br> 
 * @author wangmeng
 */
public class RealRole extends AbstractRole{

	@Override
	void response() {
		System.out.println("When a man loves me forever, I will get married with him.");
	}
	
}
