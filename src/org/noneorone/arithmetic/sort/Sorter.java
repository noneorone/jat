package org.noneorone.arithmetic.sort;

/** 
 * Title: JavaTech<br> 
 * Description: Sorter<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 8, 2011 3:16:06 PM <br> 
 * @author wangmeng
 */
public class Sorter {

	private Sorter(){}
	
	/**
	 * Bubble
	 * @param array
	 */
	public static void bubbleSort(Double[] array){
		double temp = 0;
		for(int i=0; i<array.length; i++){
			for(int j=0; j<array.length - i -1; j++){
				if(array[j]<array[j+1]){
					temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
		}
	}
	
}
