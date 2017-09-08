package org.noneorone.lang.object;

import java.io.Serializable;

public class LittleMrs implements Serializable{

	private static final long serialVersionUID = 9025187418041073709L;
	
	private int age;
	private boolean hasBaby;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isHasBaby() {
		return hasBaby;
	}

	public void setHasBaby(boolean hasBaby) {
		this.hasBaby = hasBaby;
	}

}
