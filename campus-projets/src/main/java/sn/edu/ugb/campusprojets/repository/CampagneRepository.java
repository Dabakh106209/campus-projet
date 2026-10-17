
package sn.edu.ugb.campusprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.ugb.campusprojets.entity.Campagne;

import java.util.Optional;

public interface CampagneRepository extends JpaRepository<Campagne, Long> {

    Optional<Campagne> findByNom(String nom);
}
