package org.noneorone.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
* Title: JavaTech<br>
* Description: Reflection Case.<br>
* Copyright: Copyright (c) 2012 <br>
* Create DateTime: Aug. 15, 2012 3:05:05 PM <br>
* @author wangmeng
*/
public class ReflectASMCase {

	/**
	 * Common Reflection Handler.
	 * @param clazz  the canonical name of the specified class.
	 * @param method the invoked method's name.
	 * @param args the arguments to be passed in.
	 */
	private Object commonReflect(String clazz, String method, Object args){
		Object object = null;
		try {
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class<?> cls = loader.loadClass(clazz);
			Object obj = cls.newInstance();
			Method[] methods = cls.getMethods();
			for(Method mtd : methods){
				if(mtd.getName().equalsIgnoreCase(method)){
					System.out.println(mtd.getName());
					object = mtd.invoke(obj, new Object[]{});
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public static void main(String[] args) {
		ReflectASMCase reflectASMCase = new ReflectASMCase();
		reflectASMCase.commonReflect("org.noneorone.lang.reflect.Animal", "setName", "Sariy");
		Object obj = reflectASMCase.commonReflect("org.noneorone.lang.reflect.Animal", "getName", null);
		System.out.println(obj.toString());
		
		Animal animal = new Animal();
		MethodAccess access = MethodAccess.get(Animal.class);
		access.invoke(animal, "setType", "Pig");
		String type = (String) access.invoke(animal, "getType");
		System.out.println(type);
		
	}
	
}
