package org.example.web;

import org.example.services.AuthenticationService;
import org.example.sessions.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthenticationService authService;

    public AuthController() {
        this.authService = new AuthenticationService();
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name = "login") String login,
                        @RequestParam(name = "password") String password,
                        Model model) {
        if (authService.authenticateUser(login, password)) {
            model.addAttribute("isAuthenticated", UserSession.getInstance().isAuthenticated());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Nieprawidłowy login lub hasło");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        UserSession.getInstance().logout();
        return "redirect:/";
    }
}
