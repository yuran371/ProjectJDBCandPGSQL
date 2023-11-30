package jdbcFirst.entity;

/*
 * Data Access Object (DAO)
 * 002 DAO. Entity mapping
 */

public class Ticket {
	private Long id;
	private String passengerNo;
	private String passengerName;
	private Long flightId;
	private String seatNo;
	private Long cost;
	
	public Ticket(Long id, String passengerNo, String passengerName, Long flightId, String seatNo, Long cost) {
		super();
		this.id = id;
		this.passengerNo = passengerNo;
		this.passengerName = passengerName;
		this.flightId = flightId;
		this.seatNo = seatNo;
		this.cost = cost;
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

	public Long getFlightId() {
		return flightId;
	}

	public void setFlightId(Long flightId) {
		this.flightId = flightId;
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
		return "Ticket [id=" + id + ", passengerNo=" + passengerNo + ", passengerName=" + passengerName + ", flightId="
				+ flightId + ", seatNo=" + seatNo + ", cost=" + cost + "]";
	}

	
}
