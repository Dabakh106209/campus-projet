
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.Campagne;
import sn.edu.ugb.campusprojets.entity.GroupeCandidature;
import sn.edu.ugb.campusprojets.entity.Utilisateur;
import sn.edu.ugb.campusprojets.enums.RoleUtilisateur;
import sn.edu.ugb.campusprojets.enums.TypeGroupe;
import sn.edu.ugb.campusprojets.repository.CampagneRepository;
import sn.edu.ugb.campusprojets.repository.GroupeCandidatureRepository;
import sn.edu.ugb.campusprojets.repository.UtilisateurRepository;

import java.util.List;

@RestController
@RequestMapping("/api/groupes")
public class GroupeCandidatureController {

    private final GroupeCandidatureRepository groupeRepository;
    private final CampagneRepository campagneRepository;
    private final UtilisateurRepository utilisateurRepository;

    public GroupeCandidatureController(
            GroupeCandidatureRepository groupeRepository,
            CampagneRepository campagneRepository,
            UtilisateurRepository utilisateurRepository) {
        this.groupeRepository = groupeRepository;
        this.campagneRepository = campagneRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public record GroupeRequest(
            TypeGroupe type,
            Long campagneId,
            List<Long> etudiantIds) {
    }

    public record GroupeResponse(
            Long id,
            TypeGroupe type,
            Long campagneId,
            List<Long> etudiantIds) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public GroupeResponse creer(@RequestBody GroupeRequest request) {
        if (request.type() == null || request.campagneId() == null
                || request.etudiantIds() == null) {
            throw new IllegalArgumentException(
                    "Le type, la campagne et les étudiants sont obligatoires.");
        }

        int nombre = request.etudiantIds().size();

        if (request.type() == TypeGroupe.INDIVIDUEL && nombre != 1) {
            throw new IllegalArgumentException(
                    "Un groupe individuel doit avoir un étudiant.");
        }

        if (request.type() == TypeGroupe.BINOME && nombre != 2) {
            throw new IllegalArgumentException(
                    "Un binôme doit avoir deux étudiants.");
        }

        if (request.etudiantIds().stream().distinct().count() != nombre) {
            throw new IllegalArgumentException(
                    "Un étudiant ne peut pas apparaître deux fois dans le groupe.");
        }

        Campagne campagne = campagneRepository.findById(request.campagneId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Campagne introuvable."));

        if (!Boolean.TRUE.equals(campagne.getActive())) {
            throw new IllegalArgumentException("La campagne n'est pas active.");
        }

        List<Utilisateur> membres = request.etudiantIds().stream()
                .map(id -> utilisateurRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Étudiant introuvable : " + id)))
                .toList();

        for (Utilisateur membre : membres) {
            if (membre.getRole() != RoleUtilisateur.ETUDIANT) {
                throw new IllegalArgumentException(
                        "Tous les membres doivent être des étudiants.");
            }

            if (!Boolean.TRUE.equals(membre.getActif())) {
                throw new IllegalArgumentException(
                        "Un membre du groupe est désactivé.");
            }
        }

        GroupeCandidature groupe = new GroupeCandidature();
        groupe.setType(request.type());
        groupe.setCampagne(campagne);
        groupe.setMembres(membres);

        return convertir(groupeRepository.save(groupe));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<GroupeResponse> lister() {
        return groupeRepository.findAll()
                .stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public GroupeResponse consulter(@PathVariable Long id) {
        GroupeCandidature groupe = groupeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Groupe introuvable."));

        return convertir(groupe);
    }

    @GetMapping("/campagne/{campagneId}")
    @Transactional(readOnly = true)
    public List<GroupeResponse> listerParCampagne(
            @PathVariable Long campagneId) {
        return groupeRepository.findByCampagneId(campagneId)
                .stream().map(this::convertir).toList();
    }

    private GroupeResponse convertir(GroupeCandidature groupe) {
        return new GroupeResponse(
                groupe.getId(),
                groupe.getType(),
                groupe.getCampagne().getId(),
                groupe.getMembres().stream()
                        .map(Utilisateur::getId)
                        .toList());
    }
}
