package org.noneorone.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Test {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");

	public static void main(String[] args) {
		// test(1504009409429L);
		// System.out.println(sdf.format(1505134104753L));
		// System.out.println(sdf.format(1509627090590L));
		// System.out.println(formatDate("19 Dec 2009"));

		// String webPageUrl =
		// "http://m2.tianxiaxinyong.com/promotion/landing/credit-report/per/transition.html?&sn=%E7%8E%8B%E7%8C%9B&rn=%E7%8E%8B%E6%80%9D%E6%96%87&rp=186****2665";
		// System.out.println(webPageUrl.substring(webPageUrl.indexOf("?")));
		// System.out.println(webPageUrl.substring(0, webPageUrl.indexOf("?")));

		// renameFile();

		sendPing();

	}

	private static void sendPing() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Process process = Runtime.getRuntime().exec("ping www.baidu.com");
						InputStream in = process.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						String line = null;
						while ((line = reader.readLine()) != null) {
							System.out.println(line);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						Thread.sleep(3000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private static void renameFile() {
		String path = "E:\\develop\\workshare\\temp";
		File file = new File(path);
		File[] listFiles = file.listFiles();
		for (File f : listFiles) {
			if (f.isFile()) {
				String name = f.getName();
				int lastIndex = name.lastIndexOf(".png");
				if (lastIndex != -1) {
					File dest = new File(file, name.substring(0, lastIndex).replaceAll("_页面", ""));
					f.renameTo(dest);
					System.out.println(dest.getAbsolutePath());
				}
			}
		}
	}

	private static String formatDate(String date) {
		Map<String, String> months = new HashMap<String, String>();
		months.put("Jan", "1");
		months.put("Feb", "2");
		months.put("Mar", "3");
		months.put("Apr", "4");
		months.put("May", "5");
		months.put("Jun", "6");
		months.put("Jul", "7");
		months.put("Aug", "8");
		months.put("Sept", "9");
		months.put("Oct", "10");
		months.put("Nov", "11");
		months.put("Dec", "12");
		String day = date.substring(0, date.indexOf(' '));
		String month = date.substring(date.indexOf(' ') + 1, date.lastIndexOf(' '));
		String year = date.substring(date.lastIndexOf(' ') + 1, date.length());
		return year + "年" + months.get(month) + "月" + day + "日";
	}

	private static void test(long dtMillis) {
		System.out.println("dtMillis: " + sdf.format(dtMillis));

		boolean flag = false;
		if (dtMillis > 0) {
			long nowMillis = System.currentTimeMillis();
			System.out.println("nowMillis: " + sdf.format(nowMillis));

			long minusDay = (dtMillis - nowMillis) / 1000 / 3600 / 24;

			System.out.println("minusDay: " + minusDay);

			flag = minusDay <= 30;
		}
		System.out.println("flag: " + flag);
	}

}
