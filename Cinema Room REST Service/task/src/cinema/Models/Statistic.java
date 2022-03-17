package cinema.Models;

import org.springframework.beans.factory.annotation.Autowired;

public class Statistic {

    private int currentIncome;
    private int numberOfAvailableSeats;
    private int numberOfPurchasedTickets;

    @Autowired
    public Statistic(int currentIncome, int numberOfAvailableSeats, int numberOfPurchasedTickets) {
        this.currentIncome = currentIncome;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.numberOfPurchasedTickets = numberOfPurchasedTickets;
    }

    public int getCurrentIncome() {
        return currentIncome;
    }

    public void addCurrentIncome(int currentIncome) {
        this.currentIncome += currentIncome;
    }

    public void reduceCurrentIncome(int currentIncome) {
        this.currentIncome -= currentIncome;
    }

    public int getNumberOfAvailableSeats() {
        return numberOfAvailableSeats;
    }

    public void setNumberOfAvailableSeats(int numberOfAvailableSeats) {
        this.numberOfAvailableSeats = numberOfAvailableSeats;
    }

    public int getNumberOfPurchasedTickets() {
        return numberOfPurchasedTickets;
    }

    public void addNumberOfPurchasedTickets() {
        this.numberOfPurchasedTickets++;
    }

    public void reduceNumberOfPurchasedTickets() {
        this.numberOfPurchasedTickets--;
    }
}