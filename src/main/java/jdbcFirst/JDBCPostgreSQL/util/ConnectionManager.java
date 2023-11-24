package jdbcFirst.JDBCPostgreSQL.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
	
	private static final String PASSWORD = "password1";
	private static final String USERNAME = "name";
	private static final String URL = "jdbc:postgresql://localhost:5432/flight_repository";

	static {
		loadDriver(); 
	}
	
	private static void loadDriver() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private ConnectionManager() {
	}
	
	public static Connection open() {
		try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
