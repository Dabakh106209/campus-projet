
package sn.edu.ugb.campusprojets.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sn.edu.ugb.campusprojets.entity.Affectation;
import sn.edu.ugb.campusprojets.entity.Candidature;
import sn.edu.ugb.campusprojets.entity.Utilisateur;
import sn.edu.ugb.campusprojets.enums.RoleUtilisateur;
import sn.edu.ugb.campusprojets.enums.StatutAffectation;
import sn.edu.ugb.campusprojets.enums.StatutCandidature;
import sn.edu.ugb.campusprojets.repository.AffectationRepository;
import sn.edu.ugb.campusprojets.repository.AvisEnseignantRepository;
import sn.edu.ugb.campusprojets.repository.CandidatureRepository;
import sn.edu.ugb.campusprojets.repository.UtilisateurRepository;

@Service
public class AffectationService {

    private final AffectationRepository affectationRepository;
    private final CandidatureRepository candidatureRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AvisEnseignantRepository avisRepository;

    public AffectationService(
            AffectationRepository affectationRepository,
            CandidatureRepository candidatureRepository,
            UtilisateurRepository utilisateurRepository,
            AvisEnseignantRepository avisRepository) {
        this.affectationRepository = affectationRepository;
        this.candidatureRepository = candidatureRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.avisRepository = avisRepository;
    }

    @Transactional
    public Affectation validerAffectation(
            Long responsableId, Long candidatureId) {

        Utilisateur responsable = utilisateurRepository.findById(responsableId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Responsable introuvable."));

        if (responsable.getRole()
                != RoleUtilisateur.RESPONSABLE_PEDAGOGIQUE) {
            throw new IllegalArgumentException(
                    "Seul le responsable pédagogique peut valider une affectation.");
        }

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Candidature introuvable."));

        if (candidature.getStatut() == StatutCandidature.REJETEE) {
            throw new IllegalArgumentException(
                    "Une candidature rejetée ne peut pas être affectée.");
        }

        if (!avisRepository.existsByEnseignantIdAndCandidatureId(
                candidature.getSujet().getEnseignant().getId(),
                candidatureId)) {
            throw new IllegalArgumentException(
                    "L'avis de l'enseignant est requis avant l'affectation.");
        }

        if (affectationRepository.findByCandidatureId(candidatureId).isPresent()) {
            throw new IllegalArgumentException(
                    "Cette candidature possède déjà une affectation.");
        }

        candidature.setStatut(StatutCandidature.ACCEPTEE);
        candidatureRepository.save(candidature);

        Affectation affectation = new Affectation();
        affectation.setCandidature(candidature);
        affectation.setResponsable(responsable);
        affectation.setStatut(StatutAffectation.VALIDEE);

        return affectationRepository.save(affectation);
    }
}
