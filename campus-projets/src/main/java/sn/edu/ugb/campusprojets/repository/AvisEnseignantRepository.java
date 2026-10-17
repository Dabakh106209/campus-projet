
package sn.edu.ugb.campusprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.ugb.campusprojets.entity.AvisEnseignant;

import java.util.List;

public interface AvisEnseignantRepository
        extends JpaRepository<AvisEnseignant, Long> {

    List<AvisEnseignant> findByCandidatureId(Long candidatureId);

    List<AvisEnseignant> findByEnseignantId(Long enseignantId);

    boolean existsByEnseignantIdAndCandidatureId(
        Long enseignantId, Long candidatureId);
}
