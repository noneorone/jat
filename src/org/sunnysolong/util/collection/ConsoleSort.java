package org.sunnysolong.util.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/** 
 * Title: JavaTech<br> 
 * Description:Simple Sort<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 2, 2011 8:56:25 PM <br> 
 * @author wangmeng
 */
public class ConsoleSort {

	private ConsoleSort(){}
	
	/**
	 * Reorder the input number which filled into the console.
	 */
	public static void sortInput() {
		Scanner scanner = new Scanner(System.in);
		List<String> list = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		if(scanner.hasNext()){
			String[] num = scanner.next().split(" ");
			Arrays.sort(num);
			for(int i=0;i<num.length;i++){
				for(int j=0;j<num.length-i-1;j++){
					if(num[j].equals(num[j+1])){
						set.add(num[j]);
					}
				}
			}
			for(int i=0;i<num.length;i++){
				for(Iterator<String> itr = set.iterator();itr.hasNext();){
					String n = itr.next();
					if(num[i].equals(n)){
						list.add(n);
					}
				}
			}
			for(String element : list){
				System.out.print(element+" ");
			}
		}
	}
}
