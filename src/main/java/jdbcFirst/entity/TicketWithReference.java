package jdbcFirst.entity;

/*
 * 006 DAO. Сложный entity mapping
 */

public class TicketWithReference {
	private Long id;
	private String passengerNo;
	private String passengerName;
	private Flight flight;
	private String seatNo;
	private Long cost;
	
	public TicketWithReference(Long id, String passengerNo, String passengerName, Flight flight, String seatNo, Long cost) {
		super();
		this.id = id;
		this.passengerNo = passengerNo;
		this.passengerName = passengerName;
		this.flight = flight;
		this.seatNo = seatNo;
		this.cost = cost;
	}
	
	public TicketWithReference() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassengerNo() {
		return passengerNo;
	}

	public void setPassengerNo(String passengerNo) {
		this.passengerNo = passengerNo;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public Flight getFlightId() {
		return flight;
	}

	public void setFlightId(Flight flight) {
		this.flight = flight;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", passengerNo=" + passengerNo + ", passengerName=" + passengerName + ", flight="
				+ flight + ", seatNo=" + seatNo + ", cost=" + cost + "]";
	}

	
}
