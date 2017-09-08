package org.noneorone.test;

public class ABC {

	static void exchange(int a , int b){
		System.out.println("a: " + a + " b: " + b);
		a ^= b;
		b ^= a;
		a ^= b;
		System.out.println("a: " + a + " b: " + b);
	}
	
	public static void main(String[] args) {
		exchange(1,2);
	}
	
}
