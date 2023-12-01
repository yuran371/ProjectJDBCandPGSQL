package jdbcFirst;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import jdbcFirst.dao.TicketDao;
import jdbcFirst.entity.Ticket;

/*
 * Data Access Object (DAO)
 * 003 DAO. Операции DELETE и INSERT
 */

public class DaoRunner {
	public static void main(String[] args) {
		TicketDao ticketDao = TicketDao.getInstance();
//		Ticket ticket = new Ticket();
//		ticket.setPassengerNo("wdgweg");
//		ticket.setPassengerName("qweewq");
//		ticket.setFlightId(5l);
//		ticket.setSeatNo("13A");
//		ticket.setCost(1300L);
////		Ticket createTicket = ticketDao.create(ticket);
////		System.out.println(createTicket);
////		boolean delete = ticketDao.delete(60l);
////		System.out.println(delete);
//		Optional<Ticket> findById = ticketDao.findById(2l);
//		System.out.println(findById);
		
//		Optional<Ticket> MaybeTicket = ticketDao.findById(2l);
//		MaybeTicket.ifPresent(ticketx -> {
//			ticketx.setCost(188l);
//			ticketDao.update(ticketx);
//		});
		List<Ticket> findAll = ticketDao.findAll();
		System.out.println(findAll);
	}
}
