
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.Utilisateur;
import sn.edu.ugb.campusprojets.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public record LoginRequest(
            String email,
            String motDePasse) {
    }

    public record LoginResponse(
            Long id,
            String nom,
            String prenom,
            String email,
            String role) {
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Les identifiants sont obligatoires.");
        }

        Utilisateur utilisateur = authService.authentifier(
                request.email(),
                request.motDePasse());

        return new LoginResponse(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getRole().name());
    }
}
