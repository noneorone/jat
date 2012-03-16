package org.noneorone.arithmetic.sort;

/** 
 * Title: JavaTech<br> 
 * Description: Test<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 8, 2011 3:17:45 PM <br> 
 * @author wangmeng
 */
public class Commission {

	private static void print(Object[] arrs){
		for(int i=0; i<arrs.length; i++){
			System.out.print(arrs[i].toString()+"\t");
		}
		System.out.println("\r\n---------------------------------------");
	}
	
	public static void main(String[] args){
		
		Double[] arr = {23.2,78.5,19.6,18.3,56.8};
		print(arr);
		Sorter.bubbleSort(arr);
		print(arr);

	}
}
