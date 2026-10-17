
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.AvisEnseignant;
import sn.edu.ugb.campusprojets.enums.Avis;
import sn.edu.ugb.campusprojets.repository.AvisEnseignantRepository;
import sn.edu.ugb.campusprojets.service.AvisEnseignantService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/avis")
public class AvisEnseignantController {

    private final AvisEnseignantService avisService;
    private final AvisEnseignantRepository avisRepository;

    public AvisEnseignantController(
            AvisEnseignantService avisService,
            AvisEnseignantRepository avisRepository) {
        this.avisService = avisService;
        this.avisRepository = avisRepository;
    }

    public record AvisRequest(
            Long enseignantId,
            Long candidatureId,
            Avis avis,
            String commentaire) {
    }

    public record AvisResponse(
            Long id,
            Long enseignantId,
            Long candidatureId,
            Avis avis,
            String commentaire,
            LocalDateTime dateAvis) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvisResponse donnerAvis(@RequestBody AvisRequest request) {

        AvisEnseignant avis = avisService.donnerAvis(
                request.enseignantId(),
                request.candidatureId(),
                request.avis(),
                request.commentaire());

        return convertir(avis);
    }

    @GetMapping
    public List<AvisResponse> listerTous() {
        return avisRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @GetMapping("/{id}")
    public AvisResponse consulter(@PathVariable Long id) {
        AvisEnseignant avis = avisRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Avis introuvable."));

        return convertir(avis);
    }

    @GetMapping("/candidature/{candidatureId}")
    public List<AvisResponse> listerParCandidature(
            @PathVariable Long candidatureId) {
        return avisRepository.findByCandidatureId(candidatureId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    @GetMapping("/enseignant/{enseignantId}")
    public List<AvisResponse> listerParEnseignant(
            @PathVariable Long enseignantId) {
        return avisRepository.findByEnseignantId(enseignantId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    private AvisResponse convertir(AvisEnseignant avis) {
        return new AvisResponse(
                avis.getId(),
                avis.getEnseignant().getId(),
                avis.getCandidature().getId(),
                avis.getAvis(),
                avis.getCommentaire(),
                avis.getDateAvis());
    }
}
