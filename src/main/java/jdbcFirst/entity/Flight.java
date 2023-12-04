package jdbcFirst.entity;

import java.time.LocalDateTime;

public record Flight(Long id,
					String flightNo,
					LocalDateTime departureDate,
					String departureAirportCode,
					LocalDateTime arrivalDate,
					String arrivalAirportCode,
					Long aircrfatId,
					String status) {

}
