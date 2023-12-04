package jdbcFirst;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import jdbcFirst.dao.TicketDao;
import jdbcFirst.dto.TicketFilter;
import jdbcFirst.entity.Ticket;
import jdbcFirst.entity.TicketWithReference;

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
//		List<Ticket> findAll = ticketDao.findAll();
//		System.out.println(findAll);
		
		// 005 DAO. Batch SELECT с фильтрацией
//		TicketFilter tf = new TicketFilter(3, 5);  		// ispravit` TicketFilter.java
//		List<Ticket> findAll = ticketDao.findAll(tf);
//		for (int i=0; i<findAll.size(); i++) {
//			System.out.println(findAll.get(i));
//		}
		
		// 005 DAO. Batch SELECT с фильтрацией; Second example
//		TicketFilter tf2 = new TicketFilter(3, 5, null, "A1");
//		List<Ticket> findAll2 = TicketDao.getInstance().findAll(tf2);
//		for (int i=0; i<findAll2.size(); i++) {
//			System.out.println(findAll2.get(i));
//		}
		
		// 006 DAO. Сложный entity mapping
		List<TicketWithReference> findAll = TicketDao.getInstance().findAllWithReference();
		System.out.println(findAll);
	}
}
