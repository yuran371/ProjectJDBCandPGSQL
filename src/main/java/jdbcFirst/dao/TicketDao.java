package jdbcFirst.dao;

/*
 * Data Access Object (DAO)
 * 002 DAO. Entity mapping
 */

// Singleton
public class TicketDao {
	private static final TicketDao INTSTANCE = new TicketDao();
	
	private TicketDao() {
	}
	
	public TicketDao getInstance() {
		return INTSTANCE;
	}
}
