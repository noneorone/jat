package org.sunnysolong.sms.msg;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;

/**
 * @author Terry Email: yaoxinghuo at 126 dot com
 * @version create: Aug 5, 2009 11:17:23 AM
 * @version update: Oct 15, 2009 00:11:00 AM
 * @description 飞信API(fetionlib) HTTP简单调用举例
 *              另有Restlet的方式可供调用。网页版飞信首页:http://fetionlib.appspot.com/
 *              小提示：免费开通飞信：如果您的手机号没有开通飞信，可以到中国移动飞信网站查看开通，或者直接编辑短信KTFX发送到10086开通
 *              修改飞信密码：手机编辑新密码（6到16位，不能是中文或全角字符）内容发送到12520050
 *              直接在浏览器里输入以下地址（您的手机号码和密码请自行更改，密码不要包含/，朋友号码请填写自己的手机号码）：
 *              http://fetionlib.appspot.com/restlet/fetion/13812345678/password/13912345678/message
 *              以上四个用/隔开的加粗的地方，应该分别替换成：您的手机号、密码、对方手机号（可以写自己的手机号发给自己）、短信内容（不超过180字），
 *              如果是密码错误，没有开通飞信，对方不是您好友等原因不能发送都是返回提示Message Not Sent，只有成功返回OK
 *              如果要发送中文，最好用URLEncode（UTF-8编码，如“你好”Encode后为%E4%BD%A0%E5%A5%BD，现已支持）或后面举的例子（POST方式，注意调用的URL略有不同）
 *              如果您可以收到自己发给自己的短信，恭喜您，测试通过，你可以用您熟悉的语言通过POST或GET调用，调用格式请看下面Java例子，其他语言类似
 *              如有疑问或对API的接口调用方式有任何更好的建议，欢迎提出宝贵意见
 * 
 * 现已经更新支持取得好友列表、POST方式的群发(8个或8个以下好友)和定时发送群发(定时群发最多30个好友)，请看更新的例子
 * 
 * 更新近期发现有人利用本程序给他人发送轰炸短信，给他人造成严重骚扰，同时也大量消耗本站资源，已作如下限制：
 * 同一个手机号给同一个好友的发短信API以及其他的API（如：添加好友、获取好友列表等）请求间隔为30秒，30秒内的类似请求将无法完成。
 * 注：考虑到实际需要，给自己发送短信（手机号和对方好友号码相同或者群发好友里面包含自己手机号）的API请求将不会有30秒时间间隔的限制！
 * 
 * 本飞信API接口程序由Google强力驱动、免费托管，将长期保留，示例程序用到的json包，请到www.json.org下载jar包，也可到这里下载
 */
public class FeisionSMS {
	private static Log log = LogFactory.getLog(FeisionSMS.class);

	public static void main(String[] args) {
		// 测试发短信
		boolean b = fetchToSendSMS("13812345678", "12345678",
				new String[] { "13812345678" }, "TestMessage");
		System.out.println("Send Message result:" + b);

		// 测试取得好友列表
		// JSONArray friends = fetchToGetFriends("13812345678", "12345678");
		// System.out.println("friends:\r\n"+ (friends == null ? "null" :
		// friends.toString()));

		// 测试添加好友
		// int result = fetchToAddFriend("13812345678",
		// "12345678","13812345678","TestMyName", "TestFriendName");
		// System.out.println("Add Friend result:"+result);

		// 测试发送定时短信(注意是太平洋时间，所以2009-10-09 01:00 是北京时间09:00发奥)
		// String sid = fetchToSendScheduleMsg("13812345678", "12345678", new
		// String[]{"13912345678"}, "TestScheduleMessage", "2009-10-09 01:00");
		// System.out.println("sid:"+sid);

		// 测试删除定时短信
		// boolean b2 = fetchToDeleteScheduleMsg("13812345678", "12345678",
		// "123456");
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
	 * 
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

	// 把数组转化成JSONString
	private static String convertArrayToJSONString(String[] arr)
			throws Exception {
		JSONArray ja = new JSONArray();
		for (String a : arr)
			ja.put(a);// ja.add(a);//?
		return URLEncoder.encode(ja.toString(), "UTF-8");
	}

}