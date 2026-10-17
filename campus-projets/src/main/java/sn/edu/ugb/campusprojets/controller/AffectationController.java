
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.Affectation;
import sn.edu.ugb.campusprojets.enums.StatutAffectation;
import sn.edu.ugb.campusprojets.repository.AffectationRepository;
import sn.edu.ugb.campusprojets.service.AffectationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/affectations")
public class AffectationController {

    private final AffectationService affectationService;
    private final AffectationRepository affectationRepository;

    public AffectationController(
            AffectationService affectationService,
            AffectationRepository affectationRepository) {
        this.affectationService = affectationService;
        this.affectationRepository = affectationRepository;
    }

    public record AffectationRequest(
            Long responsableId,
            Long candidatureId) {
    }

    public record AffectationResponse(
            Long id,
            Long responsableId,
            Long candidatureId,
            LocalDateTime dateAffectation,
            StatutAffectation statut) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AffectationResponse valider(
            @RequestBody AffectationRequest request) {

        Affectation affectation = affectationService.validerAffectation(
                request.responsableId(),
                request.candidatureId());

        return convertir(affectation);
    }

    @GetMapping
    public List<AffectationResponse> listerToutes() {
        return affectationRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @GetMapping("/{id}")
    public AffectationResponse consulter(@PathVariable Long id) {
        Affectation affectation = affectationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Affectation introuvable."));

        return convertir(affectation);
    }

    @GetMapping("/responsable/{responsableId}")
    public List<AffectationResponse> listerParResponsable(
            @PathVariable Long responsableId) {
        return affectationRepository.findByResponsableId(responsableId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    @GetMapping("/candidature/{candidatureId}")
    public AffectationResponse consulterParCandidature(
            @PathVariable Long candidatureId) {
        Affectation affectation = affectationRepository
                .findByCandidatureId(candidatureId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Affectation introuvable."));

        return convertir(affectation);
    }

    private AffectationResponse convertir(Affectation affectation) {
        return new AffectationResponse(
                affectation.getId(),
                affectation.getResponsable().getId(),
                affectation.getCandidature().getId(),
                affectation.getDateAffectation(),
                affectation.getStatut());
    }
}
