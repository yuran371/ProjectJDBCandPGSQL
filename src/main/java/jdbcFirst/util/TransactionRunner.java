package jdbcFirst.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * JDBC Аdvanced
 * 003 Транзакции
 */

public class TransactionRunner {

	public static void main(String[] args) throws SQLException {
//		String id = "8";	// не работает, тк не совпадает тип данных
		Long id = 9l;
		String sql1 = """
				DELETE FROM ticket 
				WHERE flight_id = ?;
				""";
		String sql2 = """
				DELETE FROM flight
				WHERE id = ?;
				""";
		Connection connection = null;
		PreparedStatement preparedStatementTicket = null;
		PreparedStatement preparedStatementFlight = null;
		try {
			connection = ConnectionManager.open(); 
			
			preparedStatementTicket = connection.prepareStatement(sql1);
			preparedStatementFlight = connection.prepareStatement(sql2);
			
			connection.setAutoCommit(false);
			
			preparedStatementTicket.setLong(1, id);
			preparedStatementFlight.setLong(1, id);
			
			preparedStatementTicket.executeUpdate(); 
			preparedStatementFlight.executeUpdate();
			
			connection.commit();
		} catch (Exception e) {
			if (connection!=null) {
				connection.rollback();
			}
			throw e;
		} finally {
			if (connection!=null) {
				connection.close();
			}
			if (preparedStatementTicket!=null) {
				preparedStatementTicket.close();
			}
			if (preparedStatementFlight!=null) {
				preparedStatementFlight.close();
			}
		}
	}

}
