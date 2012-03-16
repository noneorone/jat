package org.sunnysolong.pattern.observer;

import java.util.ArrayList;
import java.util.List;

/** 
 * Title: base<br> 
 * Description: Subject<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 4, 2011 10:19:17 AM <br> 
 * @author wangmeng
 */
public class Subject {

	private List<Object> observers;
	
	/**
	 * Initial the container filled with observers. 
	 */
	public Subject(){
		observers = new ArrayList<Object>();
	}
	
	/**
	 * Register observer.
	 * @param observer
	 */
	public void register(IObserver observer){
		if(!observers.contains(observer)){
			observers.add(observer);
		}
	}
	
	/**
	 * Unregister observer.
	 * @param observer
	 */
	public void unRegister(IObserver observer){
		if(observers.contains(observer)){
			observers.remove(observer);
		}
	}
	
	/**
	 * Just broadcast message to every registered member.
	 * @param message
	 */
	public void notify(String message){
		System.out.println(this.hashCode() + ": " + message);
		for(Object object : observers){
			IObserver observer = (IObserver)object;
			observer.receive(message);
		}
	}
}
