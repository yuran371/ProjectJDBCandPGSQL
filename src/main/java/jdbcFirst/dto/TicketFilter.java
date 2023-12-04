package jdbcFirst.dto;

/*
 * Data Access Object (DAO)
 * 005 DAO. Batch SELECT с фильтрацией
 */
public record TicketFilter(int limit,
						   int offset,
						   String passengerName,
						   String seatNo) {
}
