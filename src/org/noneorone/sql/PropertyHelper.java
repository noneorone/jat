package org.noneorone.sql;

import java.io.IOException;
import java.util.Properties;

public class PropertyHelper {

	private String driver;
	private String url;
	private String user;
	private String password;

	public PropertyHelper(){
		try {
			Properties properties = new Properties();
			properties.load(PropertyHelper.class.getResourceAsStream(DBConstant.PROPERTY_FILE_PATH));
			this.setDriver(properties.getProperty("driver"));
			this.setUrl(properties.getProperty("url"));
			this.setUser(properties.getProperty("user"));
			this.setPassword(properties.getProperty("password"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
