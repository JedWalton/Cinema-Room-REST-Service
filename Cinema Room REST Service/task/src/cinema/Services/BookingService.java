package cinema.Services;

import cinema.Dictionary.ErrorMsgs;
import cinema.Models.Cinema;
import cinema.Models.Seat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private Cinema cinema;

    public Cinema getAvailableSeatsInfo() {
        return cinema;
    }

    public ResponseEntity<String> purchaseTicket(Seat seat) {
        ResponseEntity response = getSeatAvailability(seat.getRow(), seat.getColumn());
        if (response.getStatusCode().is2xxSuccessful()) {
            changeSeatAvailability(seat, false);
        }
        return response;
    }

    private synchronized void changeSeatAvailability(Seat seat, boolean availability) {
        Arrays.stream(cinema.getAvailableSeats())
                .filter(s -> s.equals(seat))
                .forEach(s -> s.setFree(availability));
    }

    private ResponseEntity<String> getSeatAvailability(int row, int column) {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity seatInfo;
        Optional<Seat> seatOpt = Arrays.stream(cinema.getSeats())
                .filter(s -> s.getRow() == row && s.getColumn() == column)
                .findFirst();

        if (seatOpt.isEmpty()) {
            seatInfo = new ResponseEntity(Map.of("error", ErrorMsgs.OUT_OF_BOUNDS.toString()), HttpStatus.BAD_REQUEST);
        } else if (!seatOpt.get().isAvailable()) {
            seatInfo = new ResponseEntity(Map.of("error", ErrorMsgs.NOT_AVAILABLE_TICKET.toString()), HttpStatus.BAD_REQUEST);
        } else {
            try {
                seatInfo = new ResponseEntity(objectMapper.writeValueAsString(seatOpt.get()), HttpStatus.OK);
            } catch (JsonProcessingException e) {
                seatInfo = new ResponseEntity(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return seatInfo;
    }
}