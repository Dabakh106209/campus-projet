
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.Campagne;
import sn.edu.ugb.campusprojets.entity.Sujet;
import sn.edu.ugb.campusprojets.entity.Utilisateur;
import sn.edu.ugb.campusprojets.enums.RoleUtilisateur;
import sn.edu.ugb.campusprojets.enums.StatutSujet;
import sn.edu.ugb.campusprojets.repository.CampagneRepository;
import sn.edu.ugb.campusprojets.repository.SujetRepository;
import sn.edu.ugb.campusprojets.repository.UtilisateurRepository;

import java.util.List;

@RestController
@RequestMapping("/api/sujets")
public class SujetController {

    private final SujetRepository sujetRepository;
    private final CampagneRepository campagneRepository;
    private final UtilisateurRepository utilisateurRepository;

    public SujetController(
            SujetRepository sujetRepository,
            CampagneRepository campagneRepository,
            UtilisateurRepository utilisateurRepository) {
        this.sujetRepository = sujetRepository;
        this.campagneRepository = campagneRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public record SujetRequest(
            String titre,
            String description,
            String domaine,
            Integer capaciteMax,
            StatutSujet statut,
            Long enseignantId,
            Long campagneId) {
    }

    public record SujetResponse(
            Long id,
            String titre,
            String description,
            String domaine,
            Integer capaciteMax,
            StatutSujet statut,
            Long enseignantId,
            Long campagneId) {
    }

    @GetMapping
    public List<SujetResponse> listerDisponibles() {
        return sujetRepository.findByStatut(StatutSujet.DISPONIBLE)
                .stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    public SujetResponse consulter(@PathVariable Long id) {
        return convertir(trouverSujet(id));
    }

    @GetMapping("/enseignant/{enseignantId}")
    public List<SujetResponse> listerParEnseignant(
            @PathVariable Long enseignantId) {
        return sujetRepository.findByEnseignantId(enseignantId)
                .stream().map(this::convertir).toList();
    }

    @GetMapping("/campagne/{campagneId}")
    public List<SujetResponse> listerParCampagne(
            @PathVariable Long campagneId) {
        return sujetRepository.findByCampagneId(campagneId)
                .stream().map(this::convertir).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SujetResponse creer(@RequestBody SujetRequest request) {
        verifierSujet(request);

        Utilisateur enseignant = trouverEnseignant(request.enseignantId());
        Campagne campagne = trouverCampagne(request.campagneId());

        if (!Boolean.TRUE.equals(campagne.getActive())) {
            throw new IllegalArgumentException("La campagne n'est pas active.");
        }

        Sujet sujet = new Sujet();
        appliquer(sujet, request, enseignant, campagne);

        return convertir(sujetRepository.save(sujet));
    }

    @PutMapping("/{id}")
    public SujetResponse modifier(
            @PathVariable Long id,
            @RequestBody SujetRequest request) {
        verifierSujet(request);

        Sujet sujet = trouverSujet(id);
        Utilisateur enseignant = trouverEnseignant(request.enseignantId());
        Campagne campagne = trouverCampagne(request.campagneId());

        appliquer(sujet, request, enseignant, campagne);

        return convertir(sujetRepository.save(sujet));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        Sujet sujet = trouverSujet(id);
        sujetRepository.delete(sujet);
    }

    private Sujet trouverSujet(Long id) {
        return sujetRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Sujet introuvable."));
    }

    private Utilisateur trouverEnseignant(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Utilisateur introuvable."));

        if (utilisateur.getRole() != RoleUtilisateur.ENSEIGNANT) {
            throw new IllegalArgumentException(
                    "L'utilisateur doit avoir le rôle ENSEIGNANT.");
        }
        return utilisateur;
    }

    private Campagne trouverCampagne(Long id) {
        return campagneRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Campagne introuvable."));
    }

    private void verifierSujet(SujetRequest request) {
        if (request.titre() == null || request.titre().isBlank()
                || request.description() == null
                || request.description().isBlank()) {
            throw new IllegalArgumentException(
                    "Le titre et la description sont obligatoires.");
        }

        if (request.capaciteMax() == null || request.capaciteMax() < 1) {
            throw new IllegalArgumentException(
                    "La capacité doit être au moins égale à 1.");
        }

        if (request.enseignantId() == null || request.campagneId() == null) {
            throw new IllegalArgumentException(
                    "L'enseignant et la campagne sont obligatoires.");
        }
    }

    private void appliquer(
            Sujet sujet,
            SujetRequest request,
            Utilisateur enseignant,
            Campagne campagne) {
        sujet.setTitre(request.titre().trim());
        sujet.setDescription(request.description());
        sujet.setDomaine(request.domaine());
        sujet.setCapaciteMax(request.capaciteMax());
        sujet.setStatut(request.statut() == null
                ? StatutSujet.DISPONIBLE : request.statut());
        sujet.setEnseignant(enseignant);
        sujet.setCampagne(campagne);
    }

    private SujetResponse convertir(Sujet sujet) {
        return new SujetResponse(
                sujet.getId(),
                sujet.getTitre(),
                sujet.getDescription(),
                sujet.getDomaine(),
                sujet.getCapaciteMax(),
                sujet.getStatut(),
                sujet.getEnseignant().getId(),
                sujet.getCampagne().getId());
    }
}
