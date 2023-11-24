package jdbcFirst.JDBCPostgreSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

import jdbcFirst.JDBCPostgreSQL.util.ConnectionManager;

public class JdbcRunner {
	public static void main(String[] args) throws SQLException{
		Class<Driver> driverClass = Driver.class;
		
		try (Connection connection = ConnectionManager.open()) {
			System.out.println(connection.getTransactionIsolation());
		}		
	}
}