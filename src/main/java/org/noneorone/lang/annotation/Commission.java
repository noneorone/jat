package org.noneorone.lang.annotation;

import java.lang.annotation.Annotation;

public class Commission {

	public static void main(String[] args) throws ClassNotFoundException {
		
		Class<?> clazz = Class.forName("lab.lang.annotation.Student");
		Annotation[] annotations = clazz.getAnnotations();
		boolean present = clazz.isAnnotationPresent(DatabaseTable.class);
		System.out.println(present);
		if (present) {
			DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
			System.out.println("daoClass: " + databaseTable.daoClass());
			System.out.println("tableName: " + databaseTable.tableName());
		}

		System.out.println(annotations.length);
		
		
	}
	
}
