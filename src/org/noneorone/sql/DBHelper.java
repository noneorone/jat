package org.noneorone.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

	private static volatile DBHelper helper;
	private static Connection connection;
	private static PropertyHelper propertyHelper;
	
	private DBHelper(){
		propertyHelper = new PropertyHelper();
	}
	
	/**
	 * Get DBHelper instance.
	 * @return org.noneorone.sql.DBHelper
	 */
	public static synchronized DBHelper getInstance(){
		if(null == helper){
			synchronized(DBHelper.class){
				if(null == helper){
					helper = new DBHelper();
				}
			}
		}
		return helper;
	}
	
	/**
	 * Get connection.
	 * @return java.sql.Connection
	 */
	public static Connection getConnection(){
		try {
			Class.forName(propertyHelper.getDriver());
			connection = DriverManager.getConnection(propertyHelper.getUrl(), propertyHelper.getUser(), propertyHelper.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * Close Connection.
	 */
	public static void closeConnection(){
		if(null != connection){
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Close ResultSet.
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs){
		if(null != rs){
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Close Statement.
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt){
		if(null != stmt){
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
