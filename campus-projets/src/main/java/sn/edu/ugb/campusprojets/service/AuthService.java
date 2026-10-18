
package sn.edu.ugb.campusprojets.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sn.edu.ugb.campusprojets.entity.Utilisateur;
import sn.edu.ugb.campusprojets.exception.IdentifiantsInvalidesException;
import sn.edu.ugb.campusprojets.repository.UtilisateurRepository;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder) {

        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utilisateur authentifier(String email, String motDePasse) {

        // Vérification des champs obligatoires
        if (email == null || email.isBlank()
                || motDePasse == null || motDePasse.isBlank()) {

            throw new IdentifiantsInvalidesException(
                    "L'adresse e-mail et le mot de passe sont obligatoires.");
        }

        // Recherche de l'utilisateur par son adresse e-mail
        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(email.trim())
                .orElseThrow(() -> new IdentifiantsInvalidesException(
                        "Identifiants incorrects."));

        // Vérification de l'état du compte
        if (!Boolean.TRUE.equals(utilisateur.getActif())) {
            throw new IdentifiantsInvalidesException(
                    "Ce compte est désactivé.");
        }

        // Récupération du hash enregistré en base
        String hash = utilisateur.getMotDePasseHash();

        // Vérification du mot de passe avec BCrypt
        if (hash == null
                || !hash.matches("^\\$2[aby]\\$.{56}$")
                || !passwordEncoder.matches(motDePasse, hash)) {

            throw new IdentifiantsInvalidesException(
                    "Identifiants incorrects.");
        }

        // Authentification réussie
        return utilisateur;
    }
}
