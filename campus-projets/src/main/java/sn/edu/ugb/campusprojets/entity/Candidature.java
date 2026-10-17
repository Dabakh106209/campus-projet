
package sn.edu.ugb.campusprojets.entity;

import jakarta.persistence.*;
import sn.edu.ugb.campusprojets.enums.StatutCandidature;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "candidatures",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_candidature_groupe_sujet",
            columnNames = {"groupe_id", "sujet_id"}
        ),
        @UniqueConstraint(
            name = "uk_candidature_groupe_preference",
            columnNames = {"groupe_id", "ordre_preference"}
        )
    }
)
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "groupe_id", nullable = false)
    private GroupeCandidature groupe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sujet_id", nullable = false)
    private Sujet sujet;

    @Column(name = "ordre_preference", nullable = false)
    private Integer ordrePreference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutCandidature statut = StatutCandidature.EN_ATTENTE;

    @Column(nullable = false)
    private LocalDateTime dateCandidature = LocalDateTime.now();

    public Candidature() {
    }

    public Long getId() {
        return id;
    }

    public GroupeCandidature getGroupe() {
        return groupe;
    }

    public void setGroupe(GroupeCandidature groupe) {
        this.groupe = groupe;
    }

    public Sujet getSujet() {
        return sujet;
    }

    public void setSujet(Sujet sujet) {
        this.sujet = sujet;
    }

    public Integer getOrdrePreference() {
        return ordrePreference;
    }

    public void setOrdrePreference(Integer ordrePreference) {
        this.ordrePreference = ordrePreference;
    }

    public StatutCandidature getStatut() {
        return statut;
    }

    public void setStatut(StatutCandidature statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCandidature() {
        return dateCandidature;
    }
}
