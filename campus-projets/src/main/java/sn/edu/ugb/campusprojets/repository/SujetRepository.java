
package sn.edu.ugb.campusprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.ugb.campusprojets.entity.Sujet;
import sn.edu.ugb.campusprojets.enums.StatutSujet;

import java.util.List;

public interface SujetRepository extends JpaRepository<Sujet, Long> {

    List<Sujet> findByStatut(StatutSujet statut);

    List<Sujet> findByCampagneId(Long campagneId);

    List<Sujet> findByEnseignantId(Long enseignantId);
}
