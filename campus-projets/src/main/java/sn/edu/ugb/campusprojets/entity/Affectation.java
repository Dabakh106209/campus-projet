
package sn.edu.ugb.campusprojets.entity;

import jakarta.persistence.*;
import sn.edu.ugb.campusprojets.enums.StatutAffectation;

import java.time.LocalDateTime;

@Entity
@Table(name = "affectations")
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidature_id", nullable = false, unique = true)
    private Candidature candidature;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsable_id", nullable = false)
    private Utilisateur responsable;

    @Column(nullable = false)
    private LocalDateTime dateAffectation = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutAffectation statut = StatutAffectation.VALIDEE;

    public Affectation() {
    }

    public Long getId() {
        return id;
    }

    public Candidature getCandidature() {
        return candidature;
    }

    public void setCandidature(Candidature candidature) {
        this.candidature = candidature;
    }

    public Utilisateur getResponsable() {
        return responsable;
    }

    public void setResponsable(Utilisateur responsable) {
        this.responsable = responsable;
    }

    public LocalDateTime getDateAffectation() {
        return dateAffectation;
    }

    public StatutAffectation getStatut() {
        return statut;
    }

    public void setStatut(StatutAffectation statut) {
        this.statut = statut;
    }
}
