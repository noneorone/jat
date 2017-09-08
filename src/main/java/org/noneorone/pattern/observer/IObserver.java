package org.noneorone.pattern.observer;

/** 
 * Title: base<br> 
 * Description: Observer Criterion<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 4, 2011 10:16:05 AM <br> 
 * @author wangmeng
 */
public interface IObserver {

	void receive(String message);
}
