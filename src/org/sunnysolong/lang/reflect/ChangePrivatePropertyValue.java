package org.sunnysolong.lang.reflect;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
* Title: JavaTech<br>
* Description: Change the value of the private member of a specified class.<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Jul 12, 2011 2:23:05 PM <br>
* @author wangmeng
*/
public class ChangePrivatePropertyValue {

	/**
	 * A inner class as a bean.
	 */
	class Person implements Serializable{
		
		private static final long serialVersionUID = 8875435845244822261L;
		private String corporeity;
		private String spirit;
		
		public String getCorporeity() {
			return corporeity;
		}
		public void setCorporeity(String corporeity) {
			this.corporeity = corporeity;
		}
		public String getSpirit() {
			return spirit;
		}
		public void setSpirit(String spirit) {
			this.spirit = spirit;
		}
		
	}
	
	/**
	 * Change the value of the specified property.
	 * @param obj
	 * @param property
	 * @param value
	 */
	public static void changeValue(Object obj, String property, Object value){
		Field field = null;
		try {
			//Get the specified declared field.
			field = obj.getClass().getDeclaredField(property);
			//Open the accessible socket. 
			field.setAccessible(true);
			//Inject the new value.
			field.set(obj, value);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args){
		
		Person meng = new ChangePrivatePropertyValue().new Person();
		meng.setCorporeity("man");
		meng.setSpirit("woman");
		
		System.out.println("Meng is a person who owns " + meng.getCorporeity() + " corporeity and " + meng.getSpirit() + " spirit.");
		changeValue(meng, "spirit", "man");
		System.out.println("But now, he get the " + meng.getSpirit() + " spirit at last...(^_^)");

	}
}
