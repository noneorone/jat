package org.sunnysolong.sms.mail;

public class Commission {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.yeah.net");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("ifanever@yeah.net");
		mailInfo.setPassword("04251006Bm");// 您的邮箱密码
		mailInfo.setFromAddress("ifanever@yeah.net");
		mailInfo.setToAddress("wangmeng0122@gmail.com");
		mailInfo.setSubject("Java邮件发送测试");
		mailInfo.setContent("成功了吗？");
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);// 发送文体格式
		sms.sendHtmlMail(mailInfo);// 发送html格式
	}
}
