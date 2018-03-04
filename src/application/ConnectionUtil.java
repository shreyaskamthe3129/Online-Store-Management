package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	public static Connection getConnection() {
		
		Connection connection = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			
			String userName = "deepali";
			String password = "deepali";
			
			connection = DriverManager.getConnection(url,userName,password);
		
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
		
		
	}

}
