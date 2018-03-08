package org.noneorone.lang.date;

import java.text.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class DateConvertUtil {

	/**
	 * ת��Ӣ������ʱ���ʽ���ڸ�ʽ��
	 *
	 * @param dateStr
	 *            eg.2016-11-02
	 * @return
	 */
	public static Date parseENDateTime(String dateStr) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss yyyy", Locale.ENGLISH);
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ������ yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return���ض�ʱ���ʽ yyyy-MM-dd
	 */
	public static Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return�����ַ�����ʽ yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ���ض�ʱ���ַ�����ʽyyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ��ȡʱ�� Сʱ:��;�� HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * ����ʱ���ʽʱ��ת��Ϊ�ַ��� yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * ����ʱ���ʽʱ��ת��Ϊ�ַ��� yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * �õ�����ʱ��
	 * 
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * ��ȡһ�����е����һ��
	 * 
	 * @param day
	 * @return
	 */
	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 34 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * �õ�����ʱ��
	 * 
	 * @return �ַ��� yyyyMMdd HHmmss
	 */
	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * �õ�����Сʱ
	 */
	public static String getHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	/**
	 * �õ����ڷ���
	 * 
	 * @return
	 */
	public static String getTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}

	/**
	 * �����û������ʱ���ʾ��ʽ�����ص�ǰʱ��ĸ�ʽ �����yyyyMMdd��ע����ĸy���ܴ�д��
	 * 
	 * @param sformat
	 *            yyyyMMddhhmmss
	 * @return
	 */
	public static String getUserDate(String sformat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ����Сʱʱ���Ĳ�ֵ,���뱣֤����ʱ�䶼��"HH:MM"�ĸ�ʽ�������ַ��͵ķ���
	 */
	public static String getTwoHour(String st1, String st2) {
		String[] kk = null;
		String[] jj = null;
		kk = st1.split(":");
		jj = st2.split(":");
		if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
			return "0";
		else {
			double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
			double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
			if ((y - u) > 0)
				return y - u + "";
			else
				return "0";
		}
	}

	/**
	 * �õ��������ڼ�ļ������
	 */
	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * ʱ��ǰ�ƻ���Ʒ���,����JJ��ʾ����.
	 */
	public static String getPreTime(String sj1, String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(sj1);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(Time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}

	/**
	 * �õ�һ��ʱ���Ӻ��ǰ�Ƽ����ʱ��,nowdateΪʱ��,delayΪǰ�ƻ���ӵ�����
	 */
	public static String getNextDay(String nowdate, String delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date d = strToDate(nowdate);
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * �ж��Ƿ�����
	 * 
	 * @param ddate
	 * @return
	 */
	public static boolean isLeapYear(String ddate) {

		/**
		 * ��ϸ��ƣ� 1.��400���������꣬���� 2.���ܱ�4������������
		 * 3.�ܱ�4����ͬʱ���ܱ�100������������ 3.�ܱ�4����ͬʱ�ܱ�100������������
		 */
		Date d = strToDate(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * ��������ʱ���ʽ 26 Apr 2006
	 * 
	 * @param str
	 * @return
	 */
	public static String getEDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(str, pos);
		String j = strtodate.toString();
		String[] k = j.split(" ");
		return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
	}

	/**
	 * ��ȡһ���µ����һ��
	 * 
	 * @param dat
	 * @return
	 */
	public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
		String str = dat.substring(0, 8);
		String month = dat.substring(5, 7);
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
			str += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			str += "30";
		} else {
			if (isLeapYear(dat)) {
				str += "29";
			} else {
				str += "28";
			}
		}
		return str;
	}

	/**
	 * �ж϶���ʱ���Ƿ���ͬһ����
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// ���12�µ����һ�ܺ�������һ�ܵĻ������һ�ܼ���������ĵ�һ��
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * ����������,���õ���ǰʱ�����ڵ�����ǵڼ���
	 * 
	 * @return
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

	/**
	 * ���һ���������ڵ��ܵ����ڼ������ڣ���Ҫ�ҳ�2002��2��3�������ܵ�����һ�Ǽ���
	 * 
	 * @param sdate
	 * @param num
	 * @return
	 */
	public static String getWeek(String sdate, String num) {
		// ��ת��Ϊʱ��
		Date dd = DateConvertUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num.equals("1")) // ��������һ���ڵ�����
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2")) // �������ڶ����ڵ�����
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3")) // �������������ڵ�����
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4")) // �������������ڵ�����
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5")) // �������������ڵ�����
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6")) // �������������ڵ�����
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0")) // �������������ڵ�����
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * ����һ�����ڣ����������ڼ����ַ���
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getWeek(String sdate) {
		// ��ת��Ϊʱ��
		Date date = DateConvertUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour�д�ľ������ڼ��ˣ��䷶Χ 1~7
		// 1=������ 7=����������������
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	public static String getWeekStr(String sdate) {
		String str = "";
		str = DateConvertUtil.getWeek(sdate);
		if ("1".equals(str)) {
			str = "������";
		} else if ("2".equals(str)) {
			str = "����һ";
		} else if ("3".equals(str)) {
			str = "���ڶ�";
		} else if ("4".equals(str)) {
			str = "������";
		} else if ("5".equals(str)) {
			str = "������";
		} else if ("6".equals(str)) {
			str = "������";
		} else if ("7".equals(str)) {
			str = "������";
		}
		return str;
	}

	/**
	 * ����ʱ��֮�������
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// ת��Ϊ��׼ʱ��
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * �γ����µ����� �� ���ݴ����һ��ʱ�䷵��һ���ṹ ������ ����һ ���ڶ� ������ ������
	 * ������ ������ �����ǵ��µĸ���ʱ�� �˺������ظ�������һ�����������ڵ�����
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getNowMonth(String sdate) {
		// ȡ��ʱ�������µ�һ��
		sdate = sdate.substring(0, 8) + "01";

		// �õ�����µ�1�������ڼ�
		Date date = DateConvertUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int u = c.get(Calendar.DAY_OF_WEEK);
		String newday = DateConvertUtil.getNextDay(sdate, (1 - u) + "");
		return newday;
	}

	/**
	 * ȡ�����ݿ����� ���ɸ�ʽΪyyyymmddhhmmss+kλ�����
	 * 
	 * @param k
	 *            ��ʾ��ȡ��λ������������Լ���
	 */

	public static String getNo(int k) {

		return getUserDate("yyyyMMddhhmmss") + getRandom(k);
	}

	/**
	 * ����һ�������
	 * 
	 * @param i
	 * @return
	 */
	public static String getRandom(int i) {
		Random jjj = new Random();
		// int suiJiShu = jjj.nextInt(9);
		if (i == 0)
			return "";
		String jj = "";
		for (int k = 0; k < i; k++) {
			jj = jj + jjj.nextInt(9);
		}
		return jj;
	}

	/**
	 * 
	 * @param args
	 */
	public static boolean RightDate(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		;
		if (date == null)
			return false;
		if (date.length() > 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			sdf.parse(date);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	/***************************************************************************
	 * //nd=1��ʾ���ص�ֵ�а������ //yf=1��ʾ���ص�ֵ�а����·� //rq=1��ʾ���ص�ֵ�а�������
	 * //format��ʾ���صĸ�ʽ 1 �����������ķ��� 2 �Ժ���-���� // 3 ��б��/���� 4
	 * ����д��������������ʽ���� // 5 �Ե��.����
	 **************************************************************************/
	public static String getStringDateMonth(String sdate, String nd, String yf, String rq, String format) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		@SuppressWarnings("unused")
		String s_nd = dateString.substring(0, 4); // ���
		@SuppressWarnings("unused")
		String s_yf = dateString.substring(5, 7); // �·�
		@SuppressWarnings("unused")
		String s_rq = dateString.substring(8, 10); // ����
		String sreturn = "";
		// roc.util.MyChar mc = new roc.util.MyChar();
		// if (sdate == null || sdate.equals("") || !mc.Isdate(sdate)) { //
		// �����ֵ���
		// if (nd.equals("1")) {
		// sreturn = s_nd;
		// // ��������
		// if (format.equals("1"))
		// sreturn = sreturn + "��";
		// else if (format.equals("2"))
		// sreturn = sreturn + "-";
		// else if (format.equals("3"))
		// sreturn = sreturn + "/";
		// else if (format.equals("5"))
		// sreturn = sreturn + ".";
		// }
		// // �����·�
		// if (yf.equals("1")) {
		// sreturn = sreturn + s_yf;
		// if (format.equals("1"))
		// sreturn = sreturn + "��";
		// else if (format.equals("2"))
		// sreturn = sreturn + "-";
		// else if (format.equals("3"))
		// sreturn = sreturn + "/";
		// else if (format.equals("5"))
		// sreturn = sreturn + ".";
		// }
		// // ��������
		// if (rq.equals("1")) {
		// sreturn = sreturn + s_rq;
		// if (format.equals("1"))
		// sreturn = sreturn + "��";
		// }
		// } else {
		// // ���ǿ�ֵ��Ҳ��һ���Ϸ�������ֵ�����Ƚ���ת��Ϊ��׼��ʱ���ʽ
		// sdate = roc.util.RocDate.getOKDate(sdate);
		// s_nd = sdate.substring(0, 4); // ���
		// s_yf = sdate.substring(5, 7); // �·�
		// s_rq = sdate.substring(8, 10); // ����
		// if (nd.equals("1")) {
		// sreturn = s_nd;
		// // ��������
		// if (format.equals("1"))
		// sreturn = sreturn + "��";
		// else if (format.equals("2"))
		// sreturn = sreturn + "-";
		// else if (format.equals("3"))
		// sreturn = sreturn + "/";
		// else if (format.equals("5"))
		// sreturn = sreturn + ".";
		// }
		// // �����·�
		// if (yf.equals("1")) {
		// sreturn = sreturn + s_yf;
		// if (format.equals("1"))
		// sreturn = sreturn + "��";
		// else if (format.equals("2"))
		// sreturn = sreturn + "-";
		// else if (format.equals("3"))
		// sreturn = sreturn + "/";
		// else if (format.equals("5"))
		// sreturn = sreturn + ".";
		// }
		// // ��������
		// if (rq.equals("1")) {
		// sreturn = sreturn + s_rq;
		// if (format.equals("1"))
		// sreturn = sreturn + "��";
		// }
		// }
		return sreturn;
	}

	public static String getNextMonthDay(String sdate, int m) {
		sdate = getOKDate(sdate);
		int year = Integer.parseInt(sdate.substring(0, 4));
		int month = Integer.parseInt(sdate.substring(5, 7));
		month = month + m;
		if (month < 0) {
			month = month + 12;
			year = year - 1;
		} else if (month > 12) {
			month = month - 12;
			year = year + 1;
		}
		String smonth = "";
		if (month < 10)
			smonth = "0" + month;
		else
			smonth = "" + month;
		return year + "-" + smonth + "-10";
	}

	public static String getOKDate(String sdate) {
		// if (sdate == null || sdate.equals(""))
		// return getStringDateShort();
		//
		// if (!DateConvertUtil.Isdate(sdate)) {
		// sdate = getStringDateShort();
		// }
		// // ����/��ת��Ϊ��-��
		// sdate = DateConvertUtil.Replace(sdate, "/", "-");
		// // ���ֻ��8λ���ȣ���Ҫ����ת��
		// if (sdate.length() == 8)
		// sdate = sdate.substring(0, 4) + "-" + sdate.substring(4, 6) + "-"
		// + sdate.substring(6, 8);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(sdate, pos);
		String dateString = formatter.format(strtodate);
		return dateString;
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println(getTwoHour("20:30", "14:54"));
			System.out.println(Integer.valueOf(getTwoDay("2006-11-03 12:22:10", "2006-11-02 11:22:09")));
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parseENDateTime("Mon Nov 14 20:28:24 2016")));
		} catch (Exception e) {
			throw new Exception();
		}
	}

	/**
	 * 检测日期是否为截止当前日期3个月内的
	 *
	 * @param dt
	 * @return
	 */
	public static boolean isIn3MonthsTilNow(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		;

		long dtMillis = calendar.getTimeInMillis();
		long nowMillis = System.currentTimeMillis();
		long minusDay = (nowMillis - dtMillis) / 1000 / 3600 / 24;

		return minusDay <= 30 * 3;
	}

	/**
	 * 比较两个日期之间的月份之差
	 * 
	 * @param startDateInclusive
	 * @param endDateExclusive
	 * @return
	 */
	public static int monthSpace(LocalDate startDateInclusive, LocalDate endDateExclusive) {
		return Period.between(startDateInclusive, endDateExclusive).getMonths();
	}

}