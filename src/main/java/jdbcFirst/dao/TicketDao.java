package jdbcFirst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jdbcFirst.entity.Ticket;
import jdbcFirst.exception.DaoException;
import jdbcFirst.util.ConnectionManager;

/*
 * Data Access Object (DAO)
 * 002 DAO. Entity mapping
 * +
 * 003 DAO. Операции DELETE и INSERT
 */

// Singleton
public class TicketDao {
	private static final TicketDao INTSTANCE = new TicketDao();
	
	private static final String DELETE_SQL = """
			DELETE FROM ticket
			WHERE id = ?
			""";

	private static final String CREATE_SQL = """
			INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
			VALUES (?, ?, ?, ?, ?);
			""";
	
	private static final String UPDATE_SQL = """
			UPDATE ticket 
			SET passenger_no = ?,
				passenger_name = ?,
				flight_id = ?,
				seat_no = ?,
				cost = ?
			WHERE id = ?;
			""";
	
	private static final String FIND_ALL_SQL = """
			SELECT id, 
				passenger_no, 
				passenger_name, 
				flight_id, 
				seat_no, 
				cost
			FROM ticket
			""";
	
	private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + 
			"""
			WHERE id = ?
			""";
	
	private TicketDao() {
	}
	
	public boolean delete(Long id) {
		try (Connection connection = ConnectionManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(DELETE_SQL)) {
			prepareStatement.setLong(1, id);
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	public Ticket create(Ticket ticket) {
		try (Connection connection = ConnectionManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
			prepareStatement.setString(1, ticket.getPassengerNo());
			prepareStatement.setString(2, ticket.getPassengerName());
			prepareStatement.setLong(3, ticket.getFlightId());
			prepareStatement.setString(4, ticket.getSeatNo());
			prepareStatement.setLong(5, ticket.getCost());
			
			prepareStatement.executeUpdate();
			
			ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				ticket.setId(generatedKeys.getLong("id"));
			}
			
			return ticket;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	public void update(Ticket ticket) {
		try (Connection connection = ConnectionManager.get();
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
			prepareStatement.setString(1, ticket.getPassengerNo());
			prepareStatement.setString(2, ticket.getPassengerName());
			prepareStatement.setLong(3, ticket.getFlightId());
			prepareStatement.setString(4, ticket.getSeatNo());
			prepareStatement.setLong(5, ticket.getCost());
			prepareStatement.setLong(6, ticket.getId());
			
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	// 004 DAO. Операции UPDATE и SELECT
	public Optional<Ticket> findById(Long id) {
		try (Connection connection = ConnectionManager.get();
			PreparedStatement prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
			prepareStatement.setLong(1, id);
			
			ResultSet resultSet = prepareStatement.executeQuery();
			Ticket ticket = null;
			if (resultSet.next()) {
				ticket = new Ticket(
						resultSet.getLong("id"),
						resultSet.getString("passenger_no"),
						resultSet.getString("passenger_name"),
						resultSet.getLong("flight_id"),
						resultSet.getString("seat_no"),
						resultSet.getLong("cost")
				);
			}
			
			return Optional.ofNullable(ticket);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	// 004 DAO. Операции UPDATE и SELECT
	public List<Ticket> findAll() {
		try (Connection connection = ConnectionManager.get();
			PreparedStatement prepareStatement = connection.prepareStatement(FIND_ALL_SQL)) {
			ResultSet resultSet = prepareStatement.executeQuery();
			List<Ticket> tickets = new ArrayList<>();
			while (resultSet.next()) {
				tickets.add(new Ticket(
						resultSet.getLong("id"),
						resultSet.getString("passenger_no"),
						resultSet.getString("passenger_name"),
						resultSet.getLong("flight_id"),
						resultSet.getString("seat_no"),
						resultSet.getLong("cost")
				));
			}
			return tickets;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		
	}
	
	public static TicketDao getInstance() {
		return INTSTANCE;
	}
}
