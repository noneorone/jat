package org.sunnysolong.test;

import java.net.UnknownHostException;

public class Run {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws UnknownHostException{

//			System.out.println(true^true);
//			System.out.println(true^false);
//			System.out.println(false^true);
//			System.out.println(false^false);
		
		int a = 35;
		int b = 21;
		a=a+b;//a = 35 + 21 = 56;
		b=a-b;//b = 56 - 21 = 35;
		a=a-b;//a = 56 - 35 = 21;
		System.out.println(a+ "-" + b);
		System.out.println(0^0);
		System.out.println(0^1);
		System.out.println(1^0);
		System.out.println(1^1);
		System.out.println(2^1);
		System.out.println(1^2);
		
		System.out.println("====================");
		//23,68,92
		System.out.println(23^68);
		System.out.println(23^92);
		System.out.println(92^23);
		System.out.println(92^68);
		System.out.println(68^23);
		System.out.println(68^92);
	}
}
