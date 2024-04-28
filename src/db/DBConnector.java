package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	
	public static Connection getDbConnection() {
		Connection conn = null;
		String url = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/vnagara3";
		String user = "vnagara3";
		String pswd = "200473018";
		try {
			conn = DriverManager.getConnection(url, user, pswd);
			System.out.println("Successfully connected to Maria DB");
		} catch (Exception e){
			e.printStackTrace();
		} 
		return conn;
	}
	

	public static void close(Connection connection) {
		if(connection != null) {
			try {
				connection.close();
				System.out.println("Closing MariaDB Database Connection");
			} catch(Throwable whatever) {
				whatever.printStackTrace();
			}
		}
	}
	
}
