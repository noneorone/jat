package org.noneorone.lang.object;

public class Beauty implements Cloneable {

	private int age;
	private String facial;
	private boolean married;

	@Override
	public Beauty clone() {
		try {
			return (Beauty) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFacial() {
		return facial;
	}

	public void setFacial(String facial) {
		this.facial = facial;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

}
