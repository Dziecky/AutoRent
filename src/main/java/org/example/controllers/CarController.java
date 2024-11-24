package org.example.controllers;

import org.example.models.Car;
import org.example.services.CarService;

import java.util.List;

public class CarController {
    private final CarService carService;

    public CarController() {
        this.carService = new CarService();
    }

    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    public List<Car> getFilteredCars(String brand, String model, String yearFromStr, String yearToStr, String transmission, String fuelType, String dateFromStr, String dateToStr) {
        return carService.getFilteredCars(brand, model, yearFromStr, yearToStr, transmission, fuelType, dateFromStr, dateToStr);
    }

    public boolean addCar(Car car) {
        return carService.addCar(car);
    }
}