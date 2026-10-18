package sn.edu.ugb.campusprojets.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordHashGeneratorTest {

    @Test
    void genererHashBcrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String motDePasse = "CampusEtudiant2026!";
        String hash = encoder.encode(motDePasse);

        System.out.println("Mot de passe : " + motDePasse);
        System.out.println("Hash BCrypt : " + hash);
        System.out.println(
            "Vérification : " + encoder.matches(motDePasse, hash)
        );
    }
}