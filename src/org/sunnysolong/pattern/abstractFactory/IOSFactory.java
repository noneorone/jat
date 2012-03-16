package org.sunnysolong.pattern.abstractFactory;

public class IOSFactory implements OSFactory{

	public OS makeOS() {
		return new IOS();
	}

}
