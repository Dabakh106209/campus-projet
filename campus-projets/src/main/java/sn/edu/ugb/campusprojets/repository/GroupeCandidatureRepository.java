
package sn.edu.ugb.campusprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.ugb.campusprojets.entity.GroupeCandidature;

import java.util.List;

public interface GroupeCandidatureRepository
        extends JpaRepository<GroupeCandidature, Long> {

    List<GroupeCandidature> findByCampagneId(Long campagneId);

    List<GroupeCandidature> findByMembresId(Long etudiantId);
}
