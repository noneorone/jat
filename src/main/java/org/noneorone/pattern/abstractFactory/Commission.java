package org.noneorone.pattern.abstractFactory;

public class Commission {

	public static void main(String[] args){
		
		/** Factory */
		AndroidFactory androidFactory = new AndroidFactory();
		IOSFactory iosFactory = new IOSFactory();
		
		/** Worker */
		OS android = androidFactory.makeOS();
		OS ios = iosFactory.makeOS();
		
		/** Action */
		android.produce();
		ios.produce();
	}
}
