package jdbcFirst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jdbcFirst.dto.TicketFilter;
import jdbcFirst.entity.Flight;
import jdbcFirst.entity.Ticket;
import jdbcFirst.entity.TicketWithReference;
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
			SELECT ticket.id, 
				passenger_no, 
				passenger_name, 
				flight_id, 
				seat_no, 
				cost,
				f.status,						
				f.aircraft_id,				
				f.arrival_airport_code,		
				f.arrival_date,				
				f.departure_airport_code,		
				f.departure_date			
			FROM ticket 					
			JOIN flight f					
			ON ticket.flight_id = f.id			
			"""; // Dobavlen joining iz 006 DAO. Сложный entity mapping
	
	private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + 
			"""
			WHERE ticket.id = ?
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
			prepareStatement.setLong(3, ticket.getFlightId());		// Nado dobavit ticket.getFlightId().id() v sluchae, esli FLight flight (TicketWithReference)
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
				ticket = buildTicket(resultSet);
			}
			
			return Optional.ofNullable(ticket);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	public Optional<TicketWithReference> findByIdWithReference(Long id) {	//metod iz 006 DAO. Сложный entity mapping
		try (Connection connection = ConnectionManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
			prepareStatement.setLong(1, id);
			
			ResultSet resultSet = prepareStatement.executeQuery();
			TicketWithReference ticket = null;
			if (resultSet.next()) {
				ticket = buildTicketWithReference(resultSet);
			}
			
			return Optional.ofNullable(ticket);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	// 005 DAO. Batch SELECT с фильтрацией
	public List<Ticket> findAll(TicketFilter filter) {
		List<Object> filterList = new ArrayList<>();
		filterList.add(filter.limit());
		filterList.add(filter.offset());
		
		String sql = FIND_ALL_SQL + """
				LIMIT ?
				OFFSET ?
				""";
		
		try (Connection connection = ConnectionManager.get();
			PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
			
			prepareStatement.setObject(1, filterList.get(0));
			prepareStatement.setObject(2, filterList.get(1));
			
			ResultSet resultSet = prepareStatement.executeQuery();
			
			List<Ticket> list = new ArrayList<>();
			while (resultSet.next()) {
				list.add(buildTicket(resultSet));
			}
			
			return list;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	public List<Ticket> findAllWithFilter(TicketFilter filter) {
		List<Object> parameters = new ArrayList<>();
		List<String> whereSql = new ArrayList<>();
		if (filter.seatNo()!=null) {
			whereSql.add("seat_no LIKE ?");
			parameters.add("%" + filter.seatNo() + "%");
		}
		if (filter.passengerName()!=null) {
			whereSql.add("passenger_name = ?");
			parameters.add(filter.passengerName());
		}
		
		parameters.add(filter.limit());
		parameters.add(filter.offset());
		
		String where = whereSql.stream()
				.collect(Collectors.joining("AND", " WHERE ", " LIMIT ? OFFSET ? "));
		
		String sql = FIND_ALL_SQL + where;
		
		try (Connection connection = ConnectionManager.get();
				PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
			for (int i=0; i<parameters.size(); i++) {
				prepareStatement.setObject(i+1, parameters.get(i));
			}
			List<Ticket> tickets = new ArrayList<>();
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()) {
				tickets.add(buildTicket(resultSet));
			}
			return tickets;
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
				tickets.add(buildTicket(resultSet));
			}
			return tickets;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	// 006 DAO. Сложный entity mapping
	public List<TicketWithReference> findAllWithReference() {
		try (Connection connection = ConnectionManager.get();
			PreparedStatement prepareStatement = connection.prepareStatement(FIND_ALL_SQL)) {
			ResultSet resultSet = prepareStatement.executeQuery();
			List<TicketWithReference> tickets = new ArrayList<>();
			while (resultSet.next()) {
				tickets.add(buildTicketWithReference(resultSet));
			}
			return tickets;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	public static TicketDao getInstance() {
		return INTSTANCE;
	}
	
	private Ticket buildTicket(ResultSet resultSet) throws SQLException {
		return new Ticket(
				resultSet.getLong("id"),
				resultSet.getString("passenger_no"),
				resultSet.getString("passenger_name"),
				resultSet.getLong("flight_id"),
				resultSet.getString("seat_no"),
				resultSet.getLong("cost")
				);
	}
	
	private TicketWithReference buildTicketWithReference(ResultSet resultSet) throws SQLException { // 006 DAO. Сложный entity mapping
		var flight = new Flight(					// 006 DAO. Сложный entity mapping
				resultSet.getLong("flight_id"),
				resultSet.getString("flight_no"),
				resultSet.getTimestamp("departure_date").toLocalDateTime(),
				resultSet.getString("departure_airport_code"),
				resultSet.getTimestamp("arrival_date").toLocalDateTime(),
				resultSet.getString("arrival_airport_code"),
				resultSet.getLong("aircraft_id"),
				resultSet.getString("status")
				);
		return new TicketWithReference(
				resultSet.getLong("id"),
				resultSet.getString("passenger_no"),
				resultSet.getString("passenger_name"),
				flight,
				resultSet.getString("seat_no"),
				resultSet.getLong("cost")
				);
	}
}
