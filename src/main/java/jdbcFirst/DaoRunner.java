package jdbcFirst;

import jdbcFirst.dao.TicketDao;
import jdbcFirst.entity.Ticket;

/*
 * Data Access Object (DAO)
 * 003 DAO. Операции DELETE и INSERT
 */

public class DaoRunner {
	public static void main(String[] args) {
		TicketDao ticketDao = TicketDao.getInstance();
		Ticket ticket = new Ticket();
		ticket.setPassengerNo("wdgweg");
		ticket.setPassengerName("qweewq");
		ticket.setFlightId(5l);
		ticket.setSeatNo("13A");
		ticket.setCost(1300L);
//		Ticket createTicket = ticketDao.create(ticket);
		boolean delete = ticketDao.delete(60l);
		System.out.println(delete);
	}
}
