package cinema.Services;

import cinema.Dictionary.ErrorMsgs;
import cinema.Models.Cinema;
import cinema.Models.Seat;
import cinema.Models.Statistic;
import cinema.Models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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
        Ticket ticket;
        ResponseEntity response = getSeatAvailability(seat);
        if (response.getStatusCode().is2xxSuccessful()) {
            changeSeatAvailability(seat, false);
            ticket = new Ticket(seat);
            cinema.addNewActiveTicket(ticket);
            cinema.getStatistic().addCurrentIncome(ticket.getTicket().getPrice());
            cinema.getStatistic().addNumberOfPurchasedTickets();
            response = new ResponseEntity(ticket, HttpStatus.OK);
        }
        return response;
    }

    public ResponseEntity<String> returnTicket(String token) {
        ResponseEntity response;
        Optional<Ticket> ticketOpt = getTicketByToken(token);
        if (ticketOpt.isEmpty()) {
            response = new ResponseEntity(Map.of("error", ErrorMsgs.WRONG_TOKEN.toString()), HttpStatus.BAD_REQUEST);
        } else {
            Ticket ticket = ticketOpt.get();
            changeSeatAvailability(ticket.getTicket(), true);
            cinema.removeActiveTicket(ticket);
            cinema.getStatistic().reduceCurrentIncome(ticket.getTicket().getPrice());
            cinema.getStatistic().reduceNumberOfPurchasedTickets();
            response = new ResponseEntity(Map.of("returned_ticket", ticket.getTicket()), HttpStatus.OK);
        }
        return response;
    }

    public ResponseEntity<String> getStats() {
        Statistic statistic = cinema.getStatistic();
        return new ResponseEntity(statistic, HttpStatus.OK);
    }

    private Optional<Ticket> getTicketByToken(String token) {
        List<Ticket> tickets = cinema.getActiveTickets();
        Optional<Ticket> ticket = tickets.stream().filter(t -> token.equals(t.getToken())).findFirst();
        return ticket;
    }

    private synchronized void changeSeatAvailability(Seat seat, boolean isAvailable) {
        Arrays.stream(cinema.getSeats())
                .filter(s -> s.equals(seat))
                .forEach(s -> s.setAvailable(isAvailable));
    }

    private ResponseEntity<String> getSeatAvailability(Seat seat) {
        ResponseEntity seatInfo;
        Optional<Seat> seatOpt = cinema.getSeat(seat.getRow(), seat.getColumn());

        if (seatOpt.isEmpty()) {
            seatInfo = new ResponseEntity(Map.of("error", ErrorMsgs.OUT_OF_BOUNDS.toString()), HttpStatus.BAD_REQUEST);
        } else if (!seatOpt.get().isAvailable()) {
            seatInfo = new ResponseEntity(Map.of("error", ErrorMsgs.NOT_AVAILABLE_TICKET.toString()), HttpStatus.BAD_REQUEST);
        } else {
            seatInfo = new ResponseEntity(seatOpt.get(), HttpStatus.OK);
        }
        return seatInfo;
    }
}