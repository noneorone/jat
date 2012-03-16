package org.noneorone.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;


/**
 * Wap登录返回测试
* Title: JavaTech<br>
* Description: <br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Nov 14, 2011 8:55:24 PM <br>
* @author wangmeng
 */
public class WapQuery {

	private static String URL_LINK = "http://192.168.1.147/t3/wap/index.php";
//	http://192.168.1.147/t3/wap/index.php?mod=topic&code=myhome
//	转向URL：http://192.168.1.147:80/t3/wap/index.php
	private static final HttpClient HTTP_CLIENT = new HttpClient();
	private static Cookie[] cookies;
	private static GetMethod login;
	private static GetMethod home;
	
	/**
	 * 输入流转换成字符�?
	 * @param is: 输入�?
	 * @return 字符串对�?
	 */
	private static String InputStreamToString(InputStream is){
        BufferedReader reader = null;
        StringBuffer responseText = null;
        String readerText = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			responseText = new StringBuffer();
			readerText = reader.readLine();
	        while(readerText != null){
	        	responseText.append(readerText);
	        	responseText.append(System.getProperty("line.separator"));
	        	readerText = reader.readLine();
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseText.toString();
	}
	
	
	/**
	 * 将cookie写入指定文件
	 * @param cookies: cookie
	 * @param fileName: 文件�?
	 */
	private static void write(Cookie[] cookies, String fileName){
		try {
			String path = System.getProperty("user.home") + "\\" + fileName;
			File file = new File(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(Cookie c : cookies){
				bw.append(c.toString());
				bw.append(System.getProperty("line.separator"));
			}
			bw.flush();
			bw.close();
			Runtime.getRuntime().exec("explorer " + path + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 登录
	 */
	private static void login(){
		try {
			//指定链接设定请求方式
			login = new GetMethod(URL_LINK);
			//入参
			NameValuePair mod = new NameValuePair("mod", "login");
			NameValuePair code = new NameValuePair("code", "dologin");
			NameValuePair username = new NameValuePair("username", URLEncoder.encode("wangli@qq.com", "UTF-8"));
			NameValuePair password = new NameValuePair("password", URLEncoder.encode("123456", "UTF-8"));
			NameValuePair iswap = new NameValuePair("iswap", "1");
			NameValuePair cityID = new NameValuePair("cityID", URLEncoder.encode("755", "UTF-8"));
			NameValuePair city = new NameValuePair("city", URLEncoder.encode("深圳", "UTF-8"));
			NameValuePair[] params = new NameValuePair[]{mod, code, username, password, iswap, cityID, city};
			//设参
	        login.setQueryString(params);
	        //禁用重定�?
	        login.setFollowRedirects(false);
	        //执行请求并返回状态码
            int statusCode = HTTP_CLIENT.executeMethod(login);
        	//转向URL：http://192.168.1.147:80/t3/wap/index.php
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("LOGIN_ERROR >>> " + login.getStatusLine());
            }
            System.out.println("LOGIN_RESPONSE >>>" + InputStreamToString(login.getResponseBodyAsStream()));
            //设定全局cookie
            cookies = HTTP_CLIENT.getState().getCookies();
            //记录cookie
            write(cookies, "login-cookie.txt");
		} catch (UnsupportedEncodingException e) {
			System.err.println("(LOGIN) " + e.getMessage());
		} catch (HttpException e) {
			System.err.println("(LOGIN) " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("(LOGIN) " + e.getMessage());
		} finally{
			//释放连接
			login.releaseConnection();
		}

	}
	
	
	/**
	 * 主页
	 */
	private static void home(){
        try {
        	//指定链接设定请求方式
        	home = new GetMethod(URL_LINK);
        	//入参、设�?
        	home.setQueryString("mod=topic&code=myhome");
        	//记录cookie
        	write(HTTP_CLIENT.getState().getCookies(), "home-cookie.txt");
        	//添加cookie
        	HTTP_CLIENT.getState().addCookies(cookies);
        	//执行请求并返回状态码
            int statusCode = HTTP_CLIENT.executeMethod(home);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("HOME_ERROR >>>" + home.getStatusLine());
            }
            //再次记录cookie
            write(HTTP_CLIENT.getState().getCookies(), "access-cookie.txt");
            System.out.println("HOME_RESPONSE >>>" + InputStreamToString(home.getResponseBodyAsStream()));
        } catch (HttpException e) {
        	System.err.println("(HOME) " + e.getMessage());
        } catch (IOException e) {
        	System.err.println("(HOME) " + e.getMessage());
        } finally {
            home.releaseConnection();
        }
        
	}

	public static void main(String[] args) {
			//登录
			login();
			//转向主页
			home();
			
			/**
			 * 说明：查看login-cookie.txt和home-cookie.txt中记录的cookie是否完全�?致，是则表示会话信息保留完整�?
			 * access-cookie.txt中记录的是第二次请求返回的cookie，用来与全局记录的cookie做信息对比�??
			 */
    }

}