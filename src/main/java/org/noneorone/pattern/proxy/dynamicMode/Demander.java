package org.noneorone.pattern.proxy.dynamicMode;

import java.lang.reflect.Proxy;

/** 
 * Title: base<br> 
 * Description: It's a demander.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 24, 2011 8:00:05 PM <br> 
 * @author wangmeng
 */
public class Demander {

	public static void main(String[] args){
		
		RealRole realRole = new RealRole();
		
		ProxyRole handler = new ProxyRole(realRole);
		
		Class<?> cls = realRole.getClass();
		AbstractRole role = (AbstractRole) Proxy.newProxyInstance(cls.getClassLoader(),cls.getInterfaces(),handler);
		role.response();
	}
}
