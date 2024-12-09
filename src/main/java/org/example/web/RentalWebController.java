package org.example.web;

import org.example.controllers.RentalController;
import org.example.controllers.UserController;
import org.example.models.Rental;
import org.example.services.RentalService;
import org.example.sessions.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

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
                          RedirectAttributes redirectAttributes,
                          Model modelAttr) {
        if (!UserSession.getInstance().isAuthenticated() || !"USER".equals(UserSession.getInstance().getRole())) {
            return "redirect:/login";
        }
        modelAttr.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        modelAttr.addAttribute("username", UserSession.getInstance().getUsername());

        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);
        int userId = userController.getUserId(UserSession.getInstance().getUsername());

        if(from.isAfter(to)) {
            redirectAttributes.addFlashAttribute("error", "Data zwrotu musi być późniejsza niż data wypożyczenia.");
            return "redirect:/cars/" + carId;
        } else if (rentalController.rentCar(carId, userId, from, to)) {
            redirectAttributes.addFlashAttribute("message", "Samochód został pomyślnie wypożyczony!");
        } else {
            StringBuilder message = new StringBuilder("Zajęte terminy:<br>");
                            for (Rental rental : RentalService.getConflictingRentals(carId, from, to)) {
                                message.append("- Od: ").append(rental.getRentalDate())
                                        .append(" Do: ").append(rental.getReturnDate()).append("<br>");
                            }
            redirectAttributes.addFlashAttribute("error", "Samochód jest niedostępny w wybranym terminie.<br>" + message);
            return "redirect:/cars/" + carId;
        }
        return "redirect:/user-panel";
    }

    @GetMapping("/rentals")
    public String getAllRentals(Model model) {
        if (!UserSession.getInstance().isAuthenticated() || !"OWNER".equals(UserSession.getInstance().getRole())) {
            return "redirect:/";
        }
        List<Rental> rentals = rentalController.getAllRentals();
        int daysBetween;
        for(Rental rental : rentals) {
            daysBetween = (int) java.time.temporal.ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate()) + 1;
            rental.setDaysBetween(daysBetween);
            rental.setTotalPrice(rental.getCar().pricePerDay() * rental.getDaysBetween());
        }
        model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        model.addAttribute("username", UserSession.getInstance().getUsername());
        model.addAttribute("role", UserSession.getInstance().getRole());
        model.addAttribute("rentals", rentals);
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
