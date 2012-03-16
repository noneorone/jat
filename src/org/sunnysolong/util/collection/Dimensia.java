package org.sunnysolong.util.collection;

/**
* Title: JavaTech<br>
* Description: Base Array Relation.<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Apr 1st, 2011 2:51:23 PM <br>
* @author wangmeng
*/
public class Dimensia {

	private Dimensia(){}
	
	/**
	 * To show the structure of more positions.
	 * @param xL
	 * @param yL
	 * @param zL
	 */
	public static void arr(int x, int y, int z){
		int[][][] arr = new int[y][z][x];
		for(int i=0;i<y;i++){//fist loop
			for(int j=0;j<z;j++){//second loop
				for(int k=0;k<x;k++){//third loop
					arr[i][j][k]=Integer.valueOf((i+1)+""+(j+1)+""+(k+1));
				}
			}
		}
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[i].length;j++){
				for(int k=0;k<arr[i][j].length;k++){
					System.out.print(arr[i][j][k]+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	
}
