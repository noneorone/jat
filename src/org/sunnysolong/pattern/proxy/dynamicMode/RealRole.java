package org.sunnysolong.pattern.proxy.dynamicMode;

/** 
 * Title: base<br> 
 * Description: It's also a real role.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 24, 2011 7:25:15 PM <br> 
 * @author wangmeng
 */
public class RealRole implements AbstractRole {

	public void response() {
		System.out.println("When a man loves me forever, I will get married with him.");
	}

}
