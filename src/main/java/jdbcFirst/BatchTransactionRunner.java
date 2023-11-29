package jdbcFirst;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import jdbcFirst.util.ConnectionManager;

public class BatchTransactionRunner {
	public static void main(String[] args) throws SQLException {
		Long id = 8l;
		String sqlTicket = """
				DELETE FROM ticket
				WHERE flight_id = 
				""" + id;
		String sqlFlight = """
				DELETE FROM flight
				WHERE id = 
				""" + id;
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = ConnectionManager.open();
			
			connection.setAutoCommit(false);
			
			statement = connection.createStatement();
			
			statement.addBatch(sqlTicket);
			statement.addBatch(sqlFlight);
			
			statement.executeBatch();
			
			connection.commit();
		} catch (Exception e) {
			if(connection!=null) {
				connection.rollback();
			}
		} finally {
			connection.close();
			statement.close();
		}
	}
}
