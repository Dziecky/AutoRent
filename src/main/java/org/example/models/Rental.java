package org.example.models;

import java.time.LocalDate;

public class Rental {
    private int id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private Car car;
    private User user;

    private boolean canBeCancelled;
    private double totalPrice;
    private int daysBetween;

    // Konstruktor bez użytkownika
    public Rental(int id, LocalDate rentalDate, LocalDate returnDate, Car car, User user) {
        this.id = id;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.car = car;
        this.user = user;
    }
    public Rental(int id, LocalDate rentalDate, LocalDate returnDate, Car car) {
        this(id, rentalDate, returnDate, car, null);
    }

    // Gettery
    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCanBeCancelled(LocalDate currentDate) {
        canBeCancelled = currentDate.isBefore(rentalDate.minusDays(3));
    }

    public boolean getCanBeCancelled() {
        return canBeCancelled;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getDaysBetween() {
        return daysBetween;
    }

    public void setDaysBetween(int daysBetween) {
        this.daysBetween = daysBetween;
    }
}
