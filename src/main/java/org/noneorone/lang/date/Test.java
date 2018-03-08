package org.noneorone.lang.date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Test {

	/**
	 * 测试{@link DateConvertUtil#isIn3MonthsTilNow(long)}}
	 */
	@org.junit.Test
	public void test_isIn3MonthsTilNow() {

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar calendarNow = Calendar.getInstance();
		System.out.println("today>>> " + DATE_FORMAT.format(calendarNow.getTimeInMillis()));

		Random random = new Random();
		List<Map<String, Object>> data = new ArrayList<>();
		int[] months = { 9, 10, 11, 12 };
		for (int i = 0; i < 100; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, 2017);
			calendar.set(Calendar.MONTH, months[random.nextInt(months.length)]);
			calendar.set(Calendar.DATE, random.nextInt(30) + 1);
			// System.out.println(DATE_FORMAT.format(calendar.getTimeInMillis()));
			Map<String, Object> map = new HashMap<>();
			map.put("id", "26549" + i);
			map.put("createTime", calendar.getTimeInMillis() + "");
			data.add(map);
		}

		Collections.sort(data, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Long time1 = Long.valueOf(String.valueOf(o1.get("createTime")));
				Long time2 = Long.valueOf(String.valueOf(o2.get("createTime")));
				return time2.compareTo(time1);
			}
		});

		for (Map<String, Object> map : data) {
			Long createTime = Long.valueOf(String.valueOf(map.get("createTime")));
			if (DateConvertUtil.isIn3MonthsTilNow(createTime)) {
				System.out.println("In-3-Months>>> " + DATE_FORMAT.format(createTime));
			} else {
				System.err.println("Out-3-Months>>> " + DATE_FORMAT.format(createTime));
			}
		}

	}

	/**
	 * 测试日期月份比较
	 */
	@org.junit.Test
	public void test_monthSpace() {
		LocalDate endDateExclusive = LocalDate.of(2017, 12, 6);
		LocalDate startDateInclusive = LocalDate.now();
		int monthSpace = DateConvertUtil.monthSpace(startDateInclusive, endDateExclusive);
		System.out.println("monthSpace>>> " + monthSpace);
	}
}
