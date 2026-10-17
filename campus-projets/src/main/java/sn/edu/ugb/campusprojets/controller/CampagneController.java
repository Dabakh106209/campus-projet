
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.Campagne;
import sn.edu.ugb.campusprojets.repository.CampagneRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/campagnes")
public class CampagneController {

    private final CampagneRepository campagneRepository;

    public CampagneController(CampagneRepository campagneRepository) {
        this.campagneRepository = campagneRepository;
    }

    public record CampagneRequest(
            String nom,
            LocalDate dateDebut,
            LocalDate dateFin,
            Boolean active) {
    }

    public record CampagneResponse(
            Long id,
            String nom,
            LocalDate dateDebut,
            LocalDate dateFin,
            Boolean active) {
    }

    @GetMapping
    public List<CampagneResponse> lister() {
        return campagneRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @GetMapping("/{id}")
    public CampagneResponse consulter(@PathVariable Long id) {
        return convertir(trouver(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampagneResponse creer(@RequestBody CampagneRequest request) {
        verifierDates(request.dateDebut(), request.dateFin());

        if (request.nom() == null || request.nom().isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }

        if (campagneRepository.findByNom(request.nom().trim()).isPresent()) {
            throw new IllegalArgumentException("Cette campagne existe déjà.");
        }

        Campagne campagne = new Campagne();
        campagne.setNom(request.nom().trim());
        campagne.setDateDebut(request.dateDebut());
        campagne.setDateFin(request.dateFin());
        campagne.setActive(request.active() == null || request.active());

        return convertir(campagneRepository.save(campagne));
    }

    @PutMapping("/{id}")
    public CampagneResponse modifier(
            @PathVariable Long id,
            @RequestBody CampagneRequest request) {

        Campagne campagne = trouver(id);
        verifierDates(request.dateDebut(), request.dateFin());

        if (request.nom() == null || request.nom().isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }

        campagneRepository.findByNom(request.nom().trim())
                .filter(existante -> !existante.getId().equals(id))
                .ifPresent(existante -> {
                    throw new IllegalArgumentException(
                            "Ce nom de campagne est déjà utilisé.");
                });

        campagne.setNom(request.nom().trim());
        campagne.setDateDebut(request.dateDebut());
        campagne.setDateFin(request.dateFin());

        if (request.active() != null) {
            campagne.setActive(request.active());
        }

        return convertir(campagneRepository.save(campagne));
    }

    @PatchMapping("/{id}/activation")
    public CampagneResponse changerActivation(
            @PathVariable Long id,
            @RequestParam boolean active) {

        Campagne campagne = trouver(id);
        campagne.setActive(active);

        return convertir(campagneRepository.save(campagne));
    }

    private Campagne trouver(Long id) {
        return campagneRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Campagne introuvable."));
    }

    private void verifierDates(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || fin.isBefore(debut)) {
            throw new IllegalArgumentException(
                    "Les dates sont obligatoires et la fin doit être "
                    + "postérieure ou égale au début.");
        }
    }

    private CampagneResponse convertir(Campagne campagne) {
        return new CampagneResponse(
                campagne.getId(),
                campagne.getNom(),
                campagne.getDateDebut(),
                campagne.getDateFin(),
                campagne.getActive());
    }
}
