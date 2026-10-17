
package sn.edu.ugb.campusprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.ugb.campusprojets.entity.Affectation;

import java.util.List;
import java.util.Optional;

public interface AffectationRepository
        extends JpaRepository<Affectation, Long> {

    List<Affectation> findByResponsableId(Long responsableId);

    Optional<Affectation> findByCandidatureId(Long candidatureId);
}
