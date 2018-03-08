package org.noneorone.lang.object.serializable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class InfoExternal implements Externalizable {

	private int id;
	private String name;
	private int age;

	public InfoExternal() {
	}

	public InfoExternal(int id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "id>" + id + ", name>" + name + ", age>" + age;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		System.out.println(toString());
		out.writeObject(id);
		out.writeChars(name);
		out.writeObject(age);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.id = (int) in.readObject();
		this.name = (String) in.readLine();
		this.age = (int) in.readObject();
	}

}
