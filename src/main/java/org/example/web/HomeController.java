package org.example.web;

import org.example.controllers.CarController;
import org.example.models.Car;
import org.example.sessions.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Sprawdź czy użytkownik zalogowany
        model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        model.addAttribute("username", UserSession.getInstance().getUsername());
        model.addAttribute("role", UserSession.getInstance().getRole());

        List<Car> cars = new CarController().getAllCars();
        model.addAttribute("cars", cars);

        return "index";
    }
}
