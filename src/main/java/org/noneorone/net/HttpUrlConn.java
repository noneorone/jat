package org.noneorone.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpUrlConn {

	public static void main(String[] args) {

		postUrlData();

	}

	private static void postUrlData() {
		String spec = "https://www.baidu.com";
		StringBuilder content = null;
		String beginDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		URL url = null;
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		BufferedReader reader = null;
		String line;

		try {
			url = new URL(spec);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.connect();

			content = new StringBuilder();
			content.append("sldw=").append(URLEncoder.encode("440305000000", "UTF-8")).append("&");
			content.append("beginDate=").append(URLEncoder.encode(beginDate, "UTF-8")).append("&");
			content.append("type=").append(URLEncoder.encode("NEW", "UTF-8")).append("&");
			content.append("days=").append(URLEncoder.encode("7", "UTF-8"));

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(content.toString());
			outputStream.flush();
			outputStream.close();
			content.delete(0, content.length());

			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				content.append(line + "\n");
			}
			reader.close();

			System.out.println("content: " + content);

			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
