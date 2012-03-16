package org.noneorone.lang.reflect;

import java.lang.reflect.Method;

/**
* Title: JavaTech<br>
* Description: <br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Jul 13, 2011 2:56:54 PM <br>
* @author wangmeng
*/
public class MethodInvoked {

	/**
	 * To invoke specified method.
	 * @param classPath
	 * @param invokedMethod
	 * @param paramObject
	 * @return
	 */
	public static Object invoke(Object object, String invokedMethod, Object[] paramObject){
		Object obj = null;
		Class<?> cls = null;
		cls = object.getClass();
		Method[] methods = cls.getMethods();
		for(Method method : methods){
			try {
				if(method.getName().equals(invokedMethod)){
					System.out.println("methodName--> "+method.getName());
					//obj = method.invoke(object, paramObject);
//						break;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
   }
		return obj;
	}
	
	public static void main(String[] args){
//		Object obj = MethodInvoked.invoke("org.mars.lang.reflect.Person", "setName", new Object[]{"abc"});
//		Object objBak = MethodInvoked.invoke("org.mars.lang.reflect.Person", "showOptions", new Object[]{});
		Object objStr = MethodInvoked.invoke(new String(), "valueOf", new Object[]{123});
		System.out.println(objStr);
//		System.out.println(obj);
//		System.out.println(objBak);
	}
	
}
