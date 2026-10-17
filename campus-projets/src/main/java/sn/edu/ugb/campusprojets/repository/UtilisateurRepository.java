
package sn.edu.ugb.campusprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.ugb.campusprojets.entity.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository
        extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);
}
