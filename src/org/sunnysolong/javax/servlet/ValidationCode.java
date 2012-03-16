package org.sunnysolong.javax.servlet;

import javax.imageio.ImageIO; 
import javax.servlet.ServletException; 
import javax.servlet.ServletOutputStream; 
import javax.servlet.http.HttpServlet; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
* Title: demo<br>
* Description: Identifying Code from remote server.<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Jul 18, 2011 2:51:07 PM <br>
* @author wangmeng
*/
public class ValidationCode extends HttpServlet {

	private static final long serialVersionUID = 1L; 
	private static final String source = "http://192.168.1.136:8080/foshan/identifyingCode";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service(req, resp);
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		Map<String, String> cookies = null;
		String[] setCookie = null;
		String server = null;
		String sessionId = null;
		try {
			url = new URL(source);
			conn = (HttpURLConnection) url.openConnection();
			// Forbid the cache of the image.
			resp.setDateHeader("Expires", 0);
			conn.setRequestProperty("Pragma", "no-cache");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Content-Type", "image/jpeg");
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setDefaultUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(2000);
			conn.connect();
			is = conn.getInputStream();
			server = conn.getHeaderField("Server");
			setCookie = conn.getHeaderField("Set-Cookie").split(";");
			cookies = new HashMap<String, String>();
			for(String cookie : setCookie){
				String key = cookie.substring(0, cookie.indexOf('='));
				String value = cookie.substring(cookie.indexOf('=') + 1, cookie.length());
				cookies.put(key, value);
			}
			if(server.toLowerCase().contains("apache")){
				sessionId = cookies.get("JSESSIONID");
				System.out.println("sessionId: " + sessionId);
				req.getSession().setAttribute("JSESSIONID", sessionId);
			}
			ServletOutputStream out = resp.getOutputStream();
			ImageIO.write(ImageIO.read(is), "JPEG", out);
			out.flush();
			out.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
