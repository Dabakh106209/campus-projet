
package sn.edu.ugb.campusprojets.entity;

import jakarta.persistence.*;
import sn.edu.ugb.campusprojets.enums.TypeGroupe;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groupes_candidature")
public class GroupeCandidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeGroupe type = TypeGroupe.INDIVIDUEL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campagne_id", nullable = false)
    private Campagne campagne;

    @ManyToMany
    @JoinTable(
        name = "membres_groupe",
        joinColumns = @JoinColumn(name = "groupe_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id"),
        uniqueConstraints = @UniqueConstraint(
            columnNames = {"groupe_id", "etudiant_id"}
        )
    )
    private List<Utilisateur> membres = new ArrayList<>();

    public GroupeCandidature() {
    }

    public Long getId() {
        return id;
    }

    public TypeGroupe getType() {
        return type;
    }

    public void setType(TypeGroupe type) {
        this.type = type;
    }

    public Campagne getCampagne() {
        return campagne;
    }

    public void setCampagne(Campagne campagne) {
        this.campagne = campagne;
    }

    public List<Utilisateur> getMembres() {
        return membres;
    }

    public void setMembres(List<Utilisateur> membres) {
        this.membres = membres;
    }
}
