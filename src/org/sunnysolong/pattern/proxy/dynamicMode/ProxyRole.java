package org.sunnysolong.pattern.proxy.dynamicMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** 
 * Title: base<br> 
 * Description: Dynamic Proxy.<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 24, 2011 7:29:27 PM <br> 
 * @author wangmeng
 */
public class ProxyRole implements InvocationHandler{

	/**
	 * The object which would be agent.
	 */
	private Object realRole;
	
	public ProxyRole(){}
	
	/**
	 * Rebuilding the constructor in order to pass the object into the dynamic agent.
	 * @param obj
	 */
	public ProxyRole(Object object){
		this.realRole = object;
	}
	
	/**
	 * That'll invoked by the JVM automatically.
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		/**Other methods passed in can be here.*/
		
		/**Do the method of the real role.*/
		method.invoke(this.realRole, args);

		/**Other methods passed in can be here too.*/
		return null;
	}

}
