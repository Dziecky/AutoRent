package org.example.web;

import org.example.controllers.UserController;
import org.example.models.Rental;
import org.example.sessions.UserSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class UserWebController {

    private final UserController userController;

    public UserWebController() {
        this.userController = new UserController();
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        model.addAttribute("username", UserSession.getInstance().getUsername());
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam(name = "name") String name,
                           @RequestParam(name = "surname") String surname,
                           @RequestParam(name = "login") String login,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "phone") String phone,
                           Model model) {
        model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        model.addAttribute("username", UserSession.getInstance().getUsername());
        if (name.isEmpty() || surname.isEmpty() || login.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            model.addAttribute("error", "Wszystkie pola muszą być wypełnione");
            return "register";
        } else if (userController.checkIfLoginExists(login)) {
            model.addAttribute("error", "Podany login jest już zajęty");
            return "register";
        } else if (userController.registerUser(name, surname, login, BCrypt.hashpw(password, BCrypt.gensalt()), email, phone)) {
            model.addAttribute("message", "Zarejestrowano pomyślnie");
            return "login";
        } else {
            model.addAttribute("error", "Nie udało się zarejestrować użytkownika");
            return "register";
        }
    }

    @GetMapping("/user-panel")
    public String userPanel(Model model) {
        if (!UserSession.getInstance().isAuthenticated()) {
            return "redirect:/login";
        }
        String username = UserSession.getInstance().getUsername();
        List<Rental> currentRentals = userController.getUserCurrentRentals(username);
        for (Rental rental :currentRentals){
            rental.setCanBeCancelled(LocalDate.now());
        }

        model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        model.addAttribute("username", username);
        model.addAttribute("name", userController.getUserName(username));
        model.addAttribute("surname", userController.getUserSurname(username));
        model.addAttribute("email", userController.getUserEmail(username));
        model.addAttribute("phone", userController.getUserPhone(username));
        model.addAttribute("currentRentals", currentRentals);
        model.addAttribute("pastRentals", userController.getUserPastRentals(username));
        model.addAttribute("role", UserSession.getInstance().getRole());
        return "user-panel";
    }

    @GetMapping("/user-panel/update")
    public String showUpdateUserDataForm(Model model) {
        if (!UserSession.getInstance().isAuthenticated()) {
            return "redirect:/login";
        }
        String username = UserSession.getInstance().getUsername();
        model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        model.addAttribute("username", username);
        model.addAttribute("name", userController.getUserName(username));
        model.addAttribute("surname", userController.getUserSurname(username));
        model.addAttribute("email", userController.getUserEmail(username));
        model.addAttribute("phone", userController.getUserPhone(username));
        return "editUser";
    }

    @PostMapping("/user-panel/update")
    public String updateUserData(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "surname") String surname,
                                 @RequestParam(name = "email") String email,
                                 @RequestParam(name = "phone") String phone,
                                 RedirectAttributes model) {
        String username = UserSession.getInstance().getUsername();
        model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
        model.addAttribute("username", UserSession.getInstance().getUsername());
        if (userController.updateUser(username, name, surname, email, phone)) {
            model.addFlashAttribute("message", "Dane zaktualizowane pomyślnie");
        } else {
            model.addFlashAttribute("error", "Nie udało się zaktualizować danych");
        }
        return "redirect:/user-panel";
    }

}
