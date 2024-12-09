package org.example.web;

import org.example.controllers.UserController;
import org.example.sessions.UserSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserWebController {

    private final UserController userController;

    public UserWebController() {
        this.userController = new UserController();
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
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
        model.addAttribute("username", username);
        model.addAttribute("name", userController.getUserName(username));
        model.addAttribute("surname", userController.getUserSurname(username));
        model.addAttribute("email", userController.getUserEmail(username));
        model.addAttribute("phone", userController.getUserPhone(username));
        model.addAttribute("currentRentals", userController.getUserCurrentRentals(username));
        model.addAttribute("pastRentals", userController.getUserPastRentals(username));
        return "user-panel";
    }

    @PostMapping("/user-panel/update")
    public String updateUserData(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "surname") String surname,
                                 @RequestParam(name = "email") String email,
                                 @RequestParam(name = "phone") String phone,
                                 Model model) {
        String username = UserSession.getInstance().getUsername();
        if (userController.updateUser(username, name, surname, email, phone)) {
            model.addAttribute("message", "Dane zaktualizowane pomyślnie");
        } else {
            model.addAttribute("error", "Nie udało się zaktualizować danych");
        }
        return "redirect:/user-panel";
    }

}
