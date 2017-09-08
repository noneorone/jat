package org.noneorone.lang.annotation;

@DatabaseTable(tableName = "comm_student", daoClass = StudentDao.class)
public class Student {
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

class StudentDao {

}