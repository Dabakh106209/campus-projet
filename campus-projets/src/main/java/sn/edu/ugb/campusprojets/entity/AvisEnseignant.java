
package sn.edu.ugb.campusprojets.entity;

import jakarta.persistence.*;
import sn.edu.ugb.campusprojets.enums.Avis;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "avis_enseignants",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_avis_enseignant_candidature",
        columnNames = {"enseignant_id", "candidature_id"}
    )
)
public class AvisEnseignant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Utilisateur enseignant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidature_id", nullable = false)
    private Candidature candidature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Avis avis;

    @Column(length = 1000)
    private String commentaire;

    @Column(nullable = false)
    private LocalDateTime dateAvis = LocalDateTime.now();

    public AvisEnseignant() {
    }

    public Long getId() {
        return id;
    }

    public Utilisateur getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Utilisateur enseignant) {
        this.enseignant = enseignant;
    }

    public Candidature getCandidature() {
        return candidature;
    }

    public void setCandidature(Candidature candidature) {
        this.candidature = candidature;
    }

    public Avis getAvis() {
        return avis;
    }

    public void setAvis(Avis avis) {
        this.avis = avis;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getDateAvis() {
        return dateAvis;
    }
}
