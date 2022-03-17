package cinema.Controllers;

import cinema.Dictionary.ErrorMsgs;
import cinema.Models.Cinema;
import cinema.Models.Seat;
import cinema.Models.Ticket;
import cinema.Services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/seats")
    public Cinema getAvailableSeatsInfo() {
        return bookingService.getAvailableSeatsInfo();
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestBody Seat seat) {
        return bookingService.purchaseTicket(seat);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnTicket(@RequestBody Ticket token) {
        return bookingService.returnTicket(token.getToken());
    }

    @PostMapping("/stats")
    public ResponseEntity<String> getStats(@RequestParam(value = "password", required = false) String password) {
        if (!("super_secret").equals(password)) {
            return new ResponseEntity(Map.of("error", ErrorMsgs.WRONG_PASSWORD.toString()), HttpStatus.UNAUTHORIZED);
        } else {
            return bookingService.getStats();
        }
    }
}