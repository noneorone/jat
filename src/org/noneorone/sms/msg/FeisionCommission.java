package org.noneorone.sms.msg;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class FeisionCommission {
	private static Log log = LogFactory.getLog(FeisionCommission.class);

	public static void main(String[] args) {
		String mobile = "13418651172";
		String pw = "04251006Bm";
		boolean b = fetchToSendSMS(mobile, pw, new String[] { "13418651172" },"TestMessage"); 
		System.out.println("Send Message result:" + b);
		// 测试发短信
		/*
		 * boolean b = fetchToSendSMS(mobile, pw, new String[] { "15810189365" },
		 * "TestMessage"); System.out.println("Send Message result:" + b);
		 */

		// 测试取得好友列表
		JSONArray friends = fetchToGetFriends(mobile, pw);
		System.out.println("friends:\r\n"
				+ (friends == null ? "null" : friends.toString()));

		// 测试添加好友
		// int result = fetchToAddFriend(mobile, pw,"13812345678","TestMyName",
		// "TestFriendName");
		// System.out.println("Add Friend result:"+result);

		// 测试发送定时短信(注意是太平洋时间，所以2009-10-09 01:00 是北京时间09:00发奥)
		// String sid = fetchToSendScheduleMsg(mobile, pw, new
		// String[]{"13912345678"}, "TestScheduleMessage", "2009-10-09 01:00");
		// System.out.println("sid:"+sid);

		// 测试删除定时短信
		// boolean b2 = fetchToDeleteScheduleMsg(mobile, pw, "123456");
		// System.out.println("schedule message delete result:"+b2);
	}

	private static final int TRY_TIMES = 3;
	private static final int TIME_OUT = 30000;

	/**
	 * 发送短消息 更简单的Get方式（不支持群发，如要群发用下面POST方式，已更新），直接在浏览器里输入以下地址,手机号码和密码请自行改掉：
	 * http://fetionlib.appspot.com/restlet/fetion/13812345678/password/13912345678/message
	 * 成功返回OK 否则返回Message Not
	 * Sent，如果要群发或者您的密码包含/或者需要提交中文消息避免可能的乱码最好请用以下的程序（POST方式） 注意参数String[]
	 * friends 中的数组可以是好友的手机号,也可以是后面用程序取到的好友的uri，详见后面取得好友列表的说明
	 * 如fetchToSendSMS("13812345678","password",new
	 * String[]{"sip:12345678@fetion.com.cn;p=5065","13916416465","tel:15912345678"},"Test");
	 * 好友数不能超过8个，如果有需要，请用程序分开来多次调用
	 * 注意：相同手机号，相同好友的请求的调用间隔要超过30秒，否则不成功（responseCode:406），但接受好友中包含你自己的手机号的请求不受30秒的限制！
	 */
	public static boolean fetchToSendSMS(String mobile, String password,
			String[] friends, String message) {
		// 加上UUID的目的是防止这样的情况，在服务器上已经成功发送短信，却在返回结果过程中遇到错误，
		// 而导致客户端继续尝试请求，此时让服务器根据UUID分辨出该请求已经发送过，避免再次发送短信。
		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < TRY_TIMES; i++) {
			int responseCode = 0;
			try {
				URL postUrl = new URL(
						"http://fetionlib.appspot.com/restlet/fetion");
				HttpURLConnection connection = (HttpURLConnection) postUrl
						.openConnection();
				connection.setConnectTimeout(TIME_OUT);
				connection.setReadTimeout(TIME_OUT);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.connect();
				DataOutputStream out = new DataOutputStream(connection
						.getOutputStream());
				String content = "mobile=" + mobile + "&uuid=" + uuid
						+ "&password=" + password + "&friend="
						+ convertArrayToJSONString(friends) + "&message="
						+ URLEncoder.encode(message, "utf-8");
				out.writeBytes(content);

				out.flush();
				out.close();

				responseCode = connection.getResponseCode();
				connection.disconnect();
				if (responseCode == 202)
					return true;
				else
					return false;
			} catch (Exception e) {
				log.warn("error fetchToSendSMS, exception:" + e.getMessage()
						+ ". tried " + i + " times");
			}
		}
		return false;
	}

	/**
	 * 取得好友列表 GET方式为：
	 * http://fetionlib.appspot.com/restlet/fetion/friendsList/13812345678/password
	 * 成功将返回JSON格式的好友列表，如果您不了解JSON格式，请先网上查阅相关知识，
	 * 如：[{"nickname":"Jerry","localname":"小张","uri":"sip:123456@fetion.com.cn;p=6012","mobile":"13912345678"}]
	 * 其中nickname是对方给自己设置的昵称，localname是您给对方设置的名字，mobile是对方公开的手机号，uri是该用户的标识符，可用于发送短信时传递的参数
	 * 注意nickname、localname、mobile 这三个字段可能为空，如果为空，将不会再JSON中显示！ 不成功返回空白
	 * 注意：相同手机号调用间隔要超过30秒，否则不成功（responseCode:406）
	 * 
	 * 您从JSONArray中取得的uri,如sip:123456@fetion.com.cn;p=6012或可能为tel:13912345678，
	 * 可直接作为参数传入上面的例子中发送短信， 如果有mobile，也可以传入mobile如13916416465，
	 * 不过有些时候，对方不公开手机号，便无法获取手机号，只有通过uri来发送短信
	 * 
	 */
	public static JSONArray fetchToGetFriends(String mobile, String password) {
		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < TRY_TIMES; i++) {
			try {
				URL postUrl = new URL(
						"http://fetionlib.appspot.com/restlet/fetion/friendsList");
				HttpURLConnection connection = (HttpURLConnection) postUrl
						.openConnection();
				connection.setConnectTimeout(TIME_OUT);
				connection.setReadTimeout(TIME_OUT);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.connect();
				DataOutputStream out = new DataOutputStream(connection
						.getOutputStream());
				String content = "mobile=" + mobile + "&uuid=" + uuid
						+ "&password=" + password;
				out.writeBytes(content);

				out.flush();
				out.close();

				int responseCode = connection.getResponseCode();
				if (responseCode == 202) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream())); // 读取结果
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					connection.disconnect();
					return new JSONArray(sb.toString());
				} else {
					connection.disconnect();
				}
			} catch (Exception e) {
				log.warn("error fetchToGetFriends, exception:" + e.getMessage()
						+ ". tried " + i + " times");
			}
		}
		return null;
	}

	/**
	 * 邀请好友 GET方式为：
	 * http://fetionlib.appspot.com/restlet/fetion/friend/13812345678/password/13912345678/MyName/FriendNickname
	 * 返回数字-1或0或1，见下面说明
	 * 
	 * @param friend
	 *            被邀请好友的手机号
	 * @param desc
	 *            您的姓名（不能超过10个字），对方收到邀请短信时，会显示这个名字，以便让对方知道您是谁
	 * @param nickname
	 *            对方的姓名（不能超过10个字），如果对方同意的话，这个名字会作为您的好友名称显示
	 * 
	 * @return -1错误或者对方手机号不支持, 0对方已经是您的好友 1成功发送邀请短信，等待对方回复是否同意
	 *         注意：相同手机号调用间隔要超过30秒，否则不成功（responseCode:406）
	 */
	public static int fetchToAddFriend(String mobile, String password,
			String friend, String desc, String nickname) {
		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < TRY_TIMES; i++) {
			int responseCode = 0;
			try {
				URL postUrl = new URL(
						"http://fetionlib.appspot.com/restlet/fetion/friend");
				HttpURLConnection connection = (HttpURLConnection) postUrl
						.openConnection();
				connection.setConnectTimeout(TIME_OUT);
				connection.setReadTimeout(TIME_OUT);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.connect();
				DataOutputStream out = new DataOutputStream(connection
						.getOutputStream());
				String content = "mobile=" + mobile + "&uuid=" + uuid
						+ "&password=" + password + "&friend=" + friend
						+ "&desc=" + URLEncoder.encode(desc, "utf-8")
						+ "&nickname=" + URLEncoder.encode(nickname, "utf-8");
				out.writeBytes(content);

				out.flush();
				out.close();

				responseCode = connection.getResponseCode();
				if (responseCode == 202) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream())); // 读取结果
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					connection.disconnect();
					JSONObject jo = new JSONObject(sb.toString());
					return jo.getInt("action");
				} else {
					connection.disconnect();
					return -1;
				}
			} catch (Exception e) {
				log.warn("error fetchToAddFriend, exception:" + e.getMessage()
						+ ". tried " + i + " times");
			}
		}
		return -1;
	}

	/**
	 * 发送定时短信 GET方式为（不支持群发，如要群发用下面POST方式，已更新）:
	 * http://fetionlib.appspot.com/restlet/fetion/schedule/13812345678/password/13912345678/message/2009-08-08%2012:18
	 * 成功返回sid号码，否则返回空白(空格)
	 * 
	 * POST方式如下
	 * 
	 * @param message
	 *            短信内容，字数不能超过180字
	 * @param date
	 *            发送日期格式为yyyy-MM-dd HH:mm，注意日期为时区为0的标准时间，北京时间的时区是8，所以要减去8小时；
	 *            如计划2009-08-08 20:18分发送，应该填写2009-08-08 12:18；
	 *            中国移动还规定日期要超出现在时间20分钟但不能超过1年。
	 * @param friends
	 *            接受短信的好友们，
	 *            其中的数组可以是好友的手机号,也可以是用程序取到的好友的uri，注意好友数不能超过30个，如果有需要，请用程序分开来多次调用
	 *            注意：相同手机号，相同好友的请求的调用间隔要超过30秒，否则不成功（responseCode:406），但接受好友中包含你自己的手机号的请求不受30秒的限制！
	 * 
	 * @return 一个sid号码，记下来如果后续要撤销短信发送，需要用到这个号码
	 */
	public static String fetchToSendScheduleMsg(String mobile, String password,
			String[] friends, String message, String date) {
		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < TRY_TIMES; i++) {
			try {
				URL postUrl = new URL(
						"http://fetionlib.appspot.com/restlet/fetion/schedule");
				HttpURLConnection connection = (HttpURLConnection) postUrl
						.openConnection();
				connection.setConnectTimeout(TIME_OUT);
				connection.setReadTimeout(TIME_OUT);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.connect();
				DataOutputStream out = new DataOutputStream(connection
						.getOutputStream());
				String content = "mobile=" + mobile + "&uuid=" + uuid
						+ "&password=" + password + "&friend="
						+ convertArrayToJSONString(friends) + "&schedule="
						+ date.replace(" ", "%20") + "&message="
						+ URLEncoder.encode(message, "utf-8");
				out.writeBytes(content);

				out.flush();
				out.close();
				int responseCode = connection.getResponseCode();
				if (responseCode == 202) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream())); // 读取结果
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					connection.disconnect();
					JSONObject jo = new JSONObject(sb.toString());
					return jo.getString("sid");
				} else {
					connection.disconnect();
					return null;
				}
			} catch (Exception e) {
				log.warn("error fetchToSaveSchedule, exception:"
						+ e.getMessage() + ". tried " + i + " times");
			}
		}
		return null;
	}

	/**
	 * 删除定时短信 GET方式为：
	 * http://fetionlib.appspot.com/restlet/fetion/scheduleDelete/13812345678/password/aglmZXRpb25saWJyGgsSB0FjY291bnQYAQwLEgdNZXNzYWdlGCQM
	 * aglmZXRpb25saWJyGgsSB0FjY291bnQYAQwLEgdNZXNzYWdlGCQM是你发送定时短信返回的sid号码，
	 * GET方式之支持一次删除一个定时短信， 如果要删除多个，请用下面的POST方式，成功返回OK，否则返回Schedule Not Deleted
	 * 注意：相同手机号调用间隔要超过30秒，否则不成功（responseCode:406）
	 * 
	 * @param sid
	 *            发送定时短信时返回的那些sid号码（不能超过10个sid），多个用数组的形式，程序会转换成JSON提交
	 * 
	 */
	public static boolean fetchToDeleteScheduleMsg(String mobile,
			String password, String[] sids) {
		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < TRY_TIMES; i++) {
			try {
				URL postUrl = new URL(
						"http://fetionlib.appspot.com/restlet/fetion/scheduleDelete");
				HttpURLConnection connection = (HttpURLConnection) postUrl
						.openConnection();
				connection.setConnectTimeout(TIME_OUT);
				connection.setReadTimeout(TIME_OUT);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.connect();
				DataOutputStream out = new DataOutputStream(connection
						.getOutputStream());
				String content = "mobile=" + mobile + "&uuid=" + uuid
						+ "&password=" + password + "&sids="
						+ convertArrayToJSONString(sids);
				out.writeBytes(content);

				out.flush();
				out.close();

				int responseCode = connection.getResponseCode();
				connection.disconnect();
				if (responseCode == 202)
					return true;
				else
					return false;
			} catch (Exception e) {
				log.warn("error fetchToDeleteSchedule, exception:"
						+ e.getMessage() + ". tried " + i + " times");
			}
		}
		return false;
	}

	// 把数组转化成JSONString
	private static String convertArrayToJSONString(String[] arr)
			throws Exception {
		JSONArray ja = new JSONArray();
		for (String a : arr)
			ja.put(a);// ja.add(a);//?
		return URLEncoder.encode(ja.toString(), "UTF-8");
	}
}