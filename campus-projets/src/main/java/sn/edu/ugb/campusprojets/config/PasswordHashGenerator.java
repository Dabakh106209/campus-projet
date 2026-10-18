
package sn.edu.ugb.campusprojets.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String motDePasse = "CampusTest2026!";
        String hash = encoder.encode(motDePasse);

        System.out.println("Hash BCrypt généré : " + hash);
        System.out.println(
            "Vérification : " + encoder.matches(motDePasse, hash)
        );
    }
}
