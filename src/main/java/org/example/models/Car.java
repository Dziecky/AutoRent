package org.example.models;

public record Car(int id, String brand, String model, int year, int power, double engineCapacity, String fuelType,
                  String transmission, int seats, double pricePerDay) {
    // Constructor
}