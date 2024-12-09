package org.example.web;

import org.example.controllers.CarController;
import org.example.models.Car;
import org.example.sessions.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class CarWebController {

    private final CarController carController;

    public CarWebController() {
        this.carController = new CarController();
    }

    @GetMapping("/cars")
    public String listCars(@RequestParam(name = "brand", required = false) String brand,
                           @RequestParam(name = "model", required = false) String model,
                           @RequestParam(name = "yearFrom", required = false) String yearFrom,
                           @RequestParam(name = "yearTo", required = false) String yearTo,
                           @RequestParam(name = "transmission", required = false) String transmission,
                           @RequestParam(name = "fuelType", required = false) String fuelType,
                           @RequestParam(name = "dateFrom", required = false) String dateFrom,
                           @RequestParam(name = "dateTo", required = false) String dateTo,
                           Model modelAttr) {
        List<Car> cars = carController.getFilteredCars(
                brand == null ? "" : brand,
                model == null ? "" : model,
                yearFrom,
                yearTo,
                transmission,
                fuelType,
                dateFrom,
                dateTo
        );

        modelAttr.addAttribute("cars", cars);
        return "cars";
    }

    @GetMapping("/cars/{id}")
    public String carDetails(@PathVariable int id, Model modelAttr) {
        List<Car> allCars = carController.getAllCars();
        Car found = allCars.stream().filter(car -> car.id() == id).findFirst().orElse(null);
        if (found == null) {
            return "redirect:/cars";
        }
        modelAttr.addAttribute("car", found);
        modelAttr.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        modelAttr.addAttribute("role", UserSession.getInstance().getRole());
        return "car-details";
    }

    @PostMapping("/cars/add")
    public String addCar(@RequestParam(name = "brand") String brand,
                         @RequestParam(name = "model") String model,
                         @RequestParam(name = "year") int year,
                         @RequestParam(name = "power") int power,
                         @RequestParam(name = "engineCapacity") double engineCapacity,
                         @RequestParam(name = "fuelType") String fuelType,
                         @RequestParam(name = "transmission") String transmission,
                         @RequestParam(name = "seats") int seats,
                         @RequestParam(name = "pricePerDay") double pricePerDay,
                         @RequestParam(name = "imageUrl") MultipartFile imageUrl,
                         Model modelAttr) {
        // Sprawdzenie roli
        if (!UserSession.getInstance().isAuthenticated() || !"OWNER".equals(UserSession.getInstance().getRole())) {
            return "redirect:/";
        }

        String image = "";

        if (!imageUrl.isEmpty()) {
            try {
                String contentType = imageUrl.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    modelAttr.addAttribute("error", "Plik musi być obrazem.");
                    return "redirect:/cars";
                }
                if (imageUrl.getSize() > 5 * 1024 * 1024) {
                    modelAttr.addAttribute("error", "Plik nie może być większy niż 5MB.");
                    return "redirect:/cars";
                }

                String fileName = java.util.UUID.randomUUID() + "_" + Paths.get(imageUrl.getOriginalFilename()).getFileName().toString();
                Path path = Paths.get("/Users/kprze/autorent/images" + File.separator + fileName);

                Files.createDirectories(path.getParent());
                Files.copy(imageUrl.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                image = "/images/" + fileName;

            } catch (IOException e) {
                e.printStackTrace();
                modelAttr.addAttribute("error", "Nie udało się przesłać zdjęcia.");
                return "redirect:/cars";
            }
        } else {
            modelAttr.addAttribute("error", "Nie wybrano zdjęcia.");
            return "redirect:/cars";
        }

        Car car = new Car(0, brand, model, year, power, engineCapacity, fuelType, transmission, seats, pricePerDay, image);
        if (carController.addCar(car)) {
            modelAttr.addAttribute("message", "Samochód został dodany pomyślnie.");
        } else {
            modelAttr.addAttribute("error", "Nie udało się dodać samochodu.");
        }
        return "redirect:/cars";
    }
}
