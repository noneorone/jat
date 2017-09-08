package org.noneorone.lang.reflect;

public class Animal {

	private String type;
	private String name;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void sing(String words){
		System.out.println(this.name + " say: " + words);
	}

}
