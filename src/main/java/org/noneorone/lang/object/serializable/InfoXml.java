package org.noneorone.lang.object.serializable;

//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class InfoXml {
//	@XmlID
//	@XmlAttribute
	private String id;

//	@XmlAttribute
	private String name;

//	@XmlAttribute
	private int age;

	public InfoXml() {
	}

	public InfoXml(String id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
}
