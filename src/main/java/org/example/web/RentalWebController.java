package org.example.web;

import org.example.controllers.RentalController;
import org.example.controllers.UserController;
import org.example.models.Rental;
import org.example.sessions.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class RentalWebController {

    private final RentalController rentalController;
    private final UserController userController;

    public RentalWebController() {
        this.rentalController = new RentalController();
        this.userController = new UserController();
    }

    @PostMapping("/rentals/rent")
    public String rentCar(@RequestParam(name = "carId") int carId,
                          @RequestParam(name = "dateFrom") String dateFrom,
                          @RequestParam(name = "dateTo") String dateTo,
                          Model modelAttr) {
        if (!UserSession.getInstance().isAuthenticated() || !"USER".equals(UserSession.getInstance().getRole())) {
            return "redirect:/login";
        }

        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);
        int userId = userController.getUserId(UserSession.getInstance().getUsername());

        if (rentalController.rentCar(carId, userId, from, to)) {
            modelAttr.addAttribute("message", "Samochód został pomyślnie wypożyczony!");
        } else {
            modelAttr.addAttribute("error", "Samochód jest niedostępny w wybranym terminie.");
        }
        return "redirect:/cars/" + carId;
    }

    @GetMapping("/rentals")
    public String getAllRentals(Model model) {
        if (!UserSession.getInstance().isAuthenticated() || !"OWNER".equals(UserSession.getInstance().getRole())) {
            return "redirect:/";
        }
        model.addAttribute("rentals", rentalController.getAllRentals());
        return "rentals";
    }

    @PostMapping("/rentals/cancel")
    public String cancelRental(@RequestParam(name = "rentalId") int rentalId, Model modelAttr) {
        if (rentalController.cancelRental(rentalId)) {
            modelAttr.addAttribute("message", "Wypożyczenie zostało anulowane.");
        } else {
            modelAttr.addAttribute("error", "Nie udało się anulować wypożyczenia.");
        }
        return "redirect:/user-panel";
    }
}
