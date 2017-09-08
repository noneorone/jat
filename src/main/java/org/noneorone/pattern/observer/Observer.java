package org.noneorone.pattern.observer;

/** 
 * Title: base<br> 
 * Description: Observer<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 4, 2011 10:17:47 AM <br> 
 * @author wangmeng
 */
public class Observer implements IObserver{

	public void receive(String message) {
		System.out.println(this.hashCode()+": I'v got it.");
	}

}
