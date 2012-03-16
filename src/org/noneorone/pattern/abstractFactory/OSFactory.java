package org.noneorone.pattern.abstractFactory;

/** 
 * Title: base<br> 
 * Description: OS Producer<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 4, 2011 11:12:05 AM <br> 
 * @author wangmeng
 */
public interface OSFactory {

	/***
	 * The factory which makes OS.
	 * @return OS
	 */
	OS makeOS();
	
}
