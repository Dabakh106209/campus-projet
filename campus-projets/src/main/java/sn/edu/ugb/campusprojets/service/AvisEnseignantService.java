
package sn.edu.ugb.campusprojets.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sn.edu.ugb.campusprojets.entity.AvisEnseignant;
import sn.edu.ugb.campusprojets.entity.Candidature;
import sn.edu.ugb.campusprojets.entity.Utilisateur;
import sn.edu.ugb.campusprojets.enums.Avis;
import sn.edu.ugb.campusprojets.enums.RoleUtilisateur;
import sn.edu.ugb.campusprojets.repository.AvisEnseignantRepository;
import sn.edu.ugb.campusprojets.repository.CandidatureRepository;
import sn.edu.ugb.campusprojets.repository.UtilisateurRepository;

@Service
public class AvisEnseignantService {

    private final AvisEnseignantRepository avisRepository;
    private final CandidatureRepository candidatureRepository;
    private final UtilisateurRepository utilisateurRepository;

    public AvisEnseignantService(
            AvisEnseignantRepository avisRepository,
            CandidatureRepository candidatureRepository,
            UtilisateurRepository utilisateurRepository) {
        this.avisRepository = avisRepository;
        this.candidatureRepository = candidatureRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional
    public AvisEnseignant donnerAvis(
            Long enseignantId,
            Long candidatureId,
            Avis avis,
            String commentaire) {

        Utilisateur enseignant = utilisateurRepository.findById(enseignantId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Enseignant introuvable."));

        if (enseignant.getRole() != RoleUtilisateur.ENSEIGNANT) {
            throw new IllegalArgumentException(
                    "Seul un enseignant peut donner un avis.");
        }

        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Candidature introuvable."));

        if (!candidature.getSujet().getEnseignant().getId()
                .equals(enseignantId)) {
            throw new IllegalArgumentException(
                    "Cet enseignant ne peut pas évaluer ce sujet.");
        }

        if (avis == null) {
            throw new IllegalArgumentException("L'avis est obligatoire.");
        }

        if (avisRepository.existsByEnseignantIdAndCandidatureId(
                enseignantId, candidatureId)) {
            throw new IllegalArgumentException(
                    "Cet enseignant a déjà donné son avis.");
        }

        AvisEnseignant nouvelAvis = new AvisEnseignant();
        nouvelAvis.setEnseignant(enseignant);
        nouvelAvis.setCandidature(candidature);
        nouvelAvis.setAvis(avis);
        nouvelAvis.setCommentaire(commentaire);

        return avisRepository.save(nouvelAvis);
    }
}
