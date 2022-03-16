package cinema.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Cinema {

    private final int totalRows = 9;
    private final int totalColumns = 9;
    private Seat[] seats = new Seat[totalColumns * totalColumns];

    public Cinema() {
        if (seats[0] == null) {
            createSeats();
        }
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    @JsonIgnore
    public Seat[] getSeats() {
        return seats.clone();
    }

    public void setSeats(Seat[] seats) {
        this.seats = seats;
    }

    public Seat[] getAvailableSeats() {
        return Arrays.stream(seats).filter(Seat::isAvailable).toArray(Seat[]::new);
    }

    private void createSeats() {
        int seatCounter = 0;
        for (int r = 1; r <= totalRows; r++) {
            for (int c = 1; c <= totalColumns; c++) {
                seats[seatCounter] = new Seat(r, c, true);
                seatCounter++;
            }
        }
    }
}