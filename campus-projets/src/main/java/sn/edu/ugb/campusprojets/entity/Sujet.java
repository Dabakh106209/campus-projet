
package sn.edu.ugb.campusprojets.entity;

import jakarta.persistence.*;
import sn.edu.ugb.campusprojets.enums.StatutSujet;

@Entity
@Table(name = "sujets")
public class Sujet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String domaine;

    @Column(nullable = false)
    private Integer capaciteMax = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutSujet statut = StatutSujet.DISPONIBLE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Utilisateur enseignant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campagne_id", nullable = false)
    private Campagne campagne;

    public Sujet() {
    }

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public Integer getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public StatutSujet getStatut() {
        return statut;
    }

    public void setStatut(StatutSujet statut) {
        this.statut = statut;
    }

    public Utilisateur getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Utilisateur enseignant) {
        this.enseignant = enseignant;
    }

    public Campagne getCampagne() {
        return campagne;
    }

    public void setCampagne(Campagne campagne) {
        this.campagne = campagne;
    }
}
