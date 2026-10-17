
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.Utilisateur;
import sn.edu.ugb.campusprojets.enums.RoleUtilisateur;
import sn.edu.ugb.campusprojets.repository.UtilisateurRepository;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public record UtilisateurRequest(
            String nom,
            String prenom,
            String email,
            String motDePasseHash,
            RoleUtilisateur role,
            Boolean actif) {
    }

    public record UtilisateurResponse(
            Long id,
            String nom,
            String prenom,
            String email,
            RoleUtilisateur role,
            Boolean actif) {
    }

    @GetMapping
    public List<UtilisateurResponse> lister() {
        return utilisateurRepository.findAll()
                .stream().map(this::convertir).toList();
    }

    @GetMapping("/{id}")
    public UtilisateurResponse consulter(@PathVariable Long id) {
        return convertir(trouver(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurResponse creer(@RequestBody UtilisateurRequest request) {
        verifier(request, true);

        if (utilisateurRepository.findByEmail(request.email().trim())
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Cette adresse email est déjà utilisée.");
        }

        Utilisateur utilisateur = new Utilisateur();
        appliquer(utilisateur, request, true);

        return convertir(utilisateurRepository.save(utilisateur));
    }

    @PutMapping("/{id}")
    public UtilisateurResponse modifier(
            @PathVariable Long id,
            @RequestBody UtilisateurRequest request) {
        verifier(request, false);

        Utilisateur utilisateur = trouver(id);

        utilisateurRepository.findByEmail(request.email().trim())
                .filter(existante -> !existante.getId().equals(id))
                .ifPresent(existante -> {
                    throw new IllegalArgumentException(
                            "Cette adresse email est déjà utilisée.");
                });

        appliquer(utilisateur, request, false);

        return convertir(utilisateurRepository.save(utilisateur));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        utilisateurRepository.delete(trouver(id));
    }

    private Utilisateur trouver(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Utilisateur introuvable."));
    }

    private void verifier(UtilisateurRequest request, boolean creation) {
        if (request.nom() == null || request.nom().isBlank()
                || request.prenom() == null || request.prenom().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.role() == null) {
            throw new IllegalArgumentException(
                    "Le nom, le prénom, l'email et le rôle sont obligatoires.");
        }

        if (creation && (request.motDePasseHash() == null
                || request.motDePasseHash().isBlank())) {
            throw new IllegalArgumentException(
                    "Le hash du mot de passe est obligatoire.");
        }
    }

    private void appliquer(
            Utilisateur utilisateur,
            UtilisateurRequest request,
            boolean creation) {
        utilisateur.setNom(request.nom().trim());
        utilisateur.setPrenom(request.prenom().trim());
        utilisateur.setEmail(request.email().trim());
        utilisateur.setRole(request.role());

        if (creation || (request.motDePasseHash() != null
                && !request.motDePasseHash().isBlank())) {
            utilisateur.setMotDePasseHash(request.motDePasseHash());
        }

        utilisateur.setActif(
                request.actif() == null
                        ? (creation || Boolean.TRUE.equals(utilisateur.getActif()))
                        : request.actif());
    }

    private UtilisateurResponse convertir(Utilisateur utilisateur) {
        return new UtilisateurResponse(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getActif());
    }
}
