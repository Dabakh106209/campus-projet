
package sn.edu.ugb.campusprojets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sn.edu.ugb.campusprojets.entity.Candidature;
import sn.edu.ugb.campusprojets.enums.StatutCandidature;
import sn.edu.ugb.campusprojets.service.CandidatureService;

import java.util.List;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    public record CandidatureRequest(
            Long groupeId,
            Long sujetId,
            Integer ordrePreference) {
    }

    public record CandidatureResponse(
            Long id,
            Long groupeId,
            Long sujetId,
            Integer ordrePreference,
            StatutCandidature statut) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidatureResponse creer(
            @RequestBody CandidatureRequest request) {

        Candidature candidature = candidatureService.creerCandidature(
                request.groupeId(),
                request.sujetId(),
                request.ordrePreference());

        return convertir(candidature);
    }

    @GetMapping("/groupe/{groupeId}")
    public List<CandidatureResponse> listerParGroupe(
            @PathVariable Long groupeId) {

        return candidatureService.listerParGroupe(groupeId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    private CandidatureResponse convertir(Candidature candidature) {
        return new CandidatureResponse(
                candidature.getId(),
                candidature.getGroupe().getId(),
                candidature.getSujet().getId(),
                candidature.getOrdrePreference(),
                candidature.getStatut());
    }
}
