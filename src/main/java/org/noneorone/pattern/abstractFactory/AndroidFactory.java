package org.noneorone.pattern.abstractFactory;

public class AndroidFactory implements OSFactory {

	public OS makeOS() {
		return new Android();
	}

}
