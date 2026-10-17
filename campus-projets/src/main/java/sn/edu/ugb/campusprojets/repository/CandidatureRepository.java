
package sn.edu.ugb.campusprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.edu.ugb.campusprojets.entity.Candidature;
import sn.edu.ugb.campusprojets.enums.StatutCandidature;

import java.util.List;

public interface CandidatureRepository
        extends JpaRepository<Candidature, Long> {

    List<Candidature> findByGroupeId(Long groupeId);

    List<Candidature> findBySujetId(Long sujetId);

    List<Candidature> findByStatut(StatutCandidature statut);

    boolean existsByGroupeIdAndSujetId(Long groupeId, Long sujetId);

boolean existsByGroupeIdAndOrdrePreference(
        Long groupeId, Integer ordrePreference);
}
