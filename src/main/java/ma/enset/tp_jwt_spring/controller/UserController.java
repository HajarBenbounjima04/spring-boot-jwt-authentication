package ma.enset.tp_jwt_spring.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "Bienvenue dans l'API securisee";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication) {
        return "Utilisateur connecte : " + authentication.getName();
    }
}