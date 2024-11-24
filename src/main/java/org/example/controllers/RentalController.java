package org.example.controllers;

import org.example.models.Rental;
import org.example.services.RentalService;

import java.time.LocalDate;
import java.util.List;

public class RentalController {
    private final RentalService rentalService;

    public RentalController() {
        this.rentalService = new RentalService();
    }

    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    public List<Rental> getConflictingRentals(int carId, LocalDate dateFrom, LocalDate dateTo) {
        return rentalService.getConflictingRentals(carId, dateFrom, dateTo);
    }

    public boolean rentCar(int carId, int userId, LocalDate rentalDate, LocalDate returnDate) {
        return rentalService.rentCar(carId, userId, rentalDate, returnDate);
    }

    public boolean cancelRental(int rentalId) {
        return rentalService.cancelRental(rentalId);
    }
}
