package org.noneorone.lang.string;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 身份证号生成器
 * 
 * @author sam.fan
 */
public class IDCardGenerator {
	private static char[] checkCode = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
	private static int[] weightArray = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	private static String[] areaCode = { "370600", "370601", "370602", "370611", "370612", "370613", "370634", "370681", "370682", "370683", "370684", "370685", "370686", "370687", "370700", "370701", "370702", "370703", "370704", "370705", "370724", "370725" };

	private static int DAYS_OF_YEAR = 365;
	private static int MIN_AGE = DAYS_OF_YEAR * 18;
	private static int MAX_AGE = DAYS_OF_YEAR * 38;

	private static int NEED_COUNT = 5;// 修改数量

	public static void main(String[] args) {
		Set<String> cardSet = new HashSet<String>();
		int count = NEED_COUNT;
		if (args.length > 0) {
			count = Integer.valueOf(args[0]);
		}
		while (cardSet.size() < count) {
			cardSet.add(getIdCardNO());
		}
		for (String idcard : cardSet) {
			System.out.println(idcard);
		}
	}

	private static String getIdCardNO() {
		Calendar calendar = Calendar.getInstance();
		StringBuilder cardNumber = new StringBuilder();
		Random random = new Random();
		int areaIndex = random.nextInt(areaCode.length);
		cardNumber.append(areaCode[areaIndex]);

		int randomDay = random.nextInt(MAX_AGE) % (MAX_AGE - MIN_AGE + 1) + MIN_AGE;
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - randomDay);
		String birthDay = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
		cardNumber.append(birthDay);

		int suffix = random.nextInt(1000);
		cardNumber.append(String.format("%03d", suffix));

		cardNumber.append(getCheckCode(cardNumber.toString()));
		return cardNumber.toString();
	}

	public static char getCheckCode(String str) {
		int sum = 0;
		char[] charArr = str.toCharArray();
		for (int i = 0; i < charArr.length; i++) {
			sum += Integer.parseInt(String.valueOf(charArr[i])) * weightArray[i];
		}
		return checkCode[sum % 11];
	}
}