package org.noneorone.lang.regex;

public class IDSecondGen {
	/**
	 *此方法，适用于第二代身份证，18位身份证号
	 */
	public static void main(String[] args){
		if (args.length == 0) {
			System.out.println("请输入您的身份证号");
			System.exit(0);
		}
		//17位加权因子，与身份证号前17位依次相乘。
		int w[] = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
		int sum = 0;//保存级数和
		for(int i = 0; i < args[0].length() -1 ; i++){
			sum += new Integer(args[0].substring(i,i+1)) * w[i];
		}
		/**
		 *校验结果，上一步计算得出的结果与11取模，得到的结果相对应的字符就是身份证最后一位，也就是校验位。例如：0对应下面数组第一个元素，以此类推。
		 */
		String sums[] = {"1","0","X","9","8","7","6","5","4","3","2"};
		if (sums[(sum%11)].equals(args[0].substring(args[0].length()-1,args[0].length()))) {//与身份证最后一位比较		
			System.out.println("身份证号码正确");

		}else {		
			System.out.println("身份证号码不正确");

		}

	}
}
