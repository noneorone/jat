package org.noneorone.lang.object.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class InfoSerial implements Serializable {

	private static final long serialVersionUID = 8839336321510955919L;

	private int id;
	private String title;
	private int age;

	public InfoSerial(int id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private void writeObject(ObjectOutputStream oos) {
		System.out.println("writeObject...");
		age = age << 2;
		try {
			oos.defaultWriteObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readObject(ObjectInputStream ois) {
		System.out.println("readObject...");
		try {
			ois.defaultReadObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		age = age >> 2; // 通过类似此种方式来混淆对象字段来达到安全性的目的
	}

}