
package sn.edu.ugb.campusprojets.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.campusprojets.entity.*;
import sn.edu.ugb.campusprojets.enums.*;

import sn.edu.ugb.campusprojets.repository.*;

@Service
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final GroupeCandidatureRepository groupeRepository;
    private final SujetRepository sujetRepository;

    public CandidatureService(
            CandidatureRepository candidatureRepository,
            GroupeCandidatureRepository groupeRepository,
            SujetRepository sujetRepository) {
        this.candidatureRepository = candidatureRepository;
        this.groupeRepository = groupeRepository;
        this.sujetRepository = sujetRepository;
    }

    @Transactional
    public Candidature creerCandidature(
            Long groupeId, Long sujetId, Integer ordrePreference) {

        if (ordrePreference == null || ordrePreference < 1) {
            throw new IllegalArgumentException(
                    "La préférence doit être un entier positif.");
        }

        GroupeCandidature groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Groupe introuvable."));

        Sujet sujet = sujetRepository.findById(sujetId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sujet introuvable."));

        int nombreMembres = groupe.getMembres().size();

        if (nombreMembres < 1 || nombreMembres > 2) {
            throw new IllegalArgumentException(
                    "Un groupe doit contenir un ou deux étudiants.");
        }

        for (Utilisateur membre : groupe.getMembres()) {
            if (membre.getRole() != RoleUtilisateur.ETUDIANT) {
                throw new IllegalArgumentException(
                        "Tous les membres doivent être des étudiants.");
            }
        }

        if (groupe.getType() == TypeGroupe.INDIVIDUEL
                && nombreMembres != 1) {
            throw new IllegalArgumentException(
                    "Un groupe individuel doit avoir un seul membre.");
        }

        if (groupe.getType() == TypeGroupe.BINOME
                && nombreMembres != 2) {
            throw new IllegalArgumentException(
                    "Un binôme doit avoir exactement deux membres.");
        }

        if (sujet.getStatut() != StatutSujet.DISPONIBLE) {
            throw new IllegalArgumentException(
                    "Ce sujet n'est pas disponible.");
        }

        if (!groupe.getCampagne().getId()
                .equals(sujet.getCampagne().getId())) {
            throw new IllegalArgumentException(
                    "Le groupe et le sujet doivent appartenir à la même campagne.");
        }

        if (!Boolean.TRUE.equals(groupe.getCampagne().getActive())) {
            throw new IllegalArgumentException(
                    "La campagne n'est pas active.");
        }

        if (candidatureRepository.existsByGroupeIdAndSujetId(
                groupeId, sujetId)) {
            throw new IllegalArgumentException(
                    "Ce groupe a déjà candidaté à ce sujet.");
        }

        if (candidatureRepository.existsByGroupeIdAndOrdrePreference(
                groupeId, ordrePreference)) {
            throw new IllegalArgumentException(
                    "Cette préférence est déjà utilisée par le groupe.");
        }

        Candidature candidature = new Candidature();
        candidature.setGroupe(groupe);
        candidature.setSujet(sujet);
        candidature.setOrdrePreference(ordrePreference);
        candidature.setStatut(StatutCandidature.EN_ATTENTE);

        return candidatureRepository.save(candidature);
    }
    
    @Transactional(readOnly = true)
    public java.util.List<Candidature> listerParGroupe(Long groupeId) {
        if (!groupeRepository.existsById(groupeId)) {
            throw new IllegalArgumentException("Groupe introuvable.");
        }

        return candidatureRepository.findByGroupeId(groupeId);
    }

}
