
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Sujet {
  id: number;
  titre: string;
  categorie: string;
  date: string;
  candidatures: number;
  statut: 'Publié' | 'Brouillon';
}

@Component({
  selector: 'app-sujets',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './sujets.html',
  styleUrl: './sujets.scss'
})
export class SujetsComponent {
  searchText = '';

  sujets: Sujet[] = [
    {
      id: 1,
      titre: 'Plateforme de gestion des projets tutorés',
      categorie: 'Développement Web',
      date: '15/11/2026',
      candidatures: 8,
      statut: 'Publié'
    },
    {
      id: 2,
      titre: 'Système intelligent de suivi agricole',
      categorie: 'Intelligence Artificielle',
      date: '20/11/2026',
      candidatures: 5,
      statut: 'Publié'
    },
    {
      id: 3,
      titre: 'Application mobile de gestion universitaire',
      categorie: 'Développement Mobile',
      date: '25/11/2026',
      candidatures: 0,
      statut: 'Brouillon'
    },
    {
      id: 4,
      titre: 'Analyse des données et aide à la décision',
      categorie: 'Data Science',
      date: '30/11/2026',
      candidatures: 3,
      statut: 'Publié'
    }
  ];

  get sujetsFiltres(): Sujet[] {
    const recherche = this.searchText.toLowerCase().trim();

    return this.sujets.filter(sujet =>
      sujet.titre.toLowerCase().includes(recherche) ||
      sujet.categorie.toLowerCase().includes(recherche)
    );
  }

  get totalCandidatures(): number {
    return this.sujets.reduce(
      (total, sujet) => total + sujet.candidatures, 0
    );
  }

  get sujetsPublies(): number {
    return this.sujets.filter(sujet => sujet.statut === 'Publié').length;
  }

  get brouillons(): number {
    return this.sujets.filter(sujet => sujet.statut === 'Brouillon').length;
  }

  proposerSujet(): void {
    alert('Le formulaire de création sera ajouté prochainement.');
  }

  consulterSujet(id: number): void {
    alert(`Consultation du sujet ${id} : fonctionnalité à venir.`);
  }

  modifierSujet(id: number): void {
    alert(`Modification du sujet ${id} : fonctionnalité à venir.`);
  }

  supprimerSujet(id: number): void {
    const confirmation = confirm(
      'Voulez-vous vraiment supprimer ce sujet de démonstration ?'
    );

    if (confirmation) {
      this.sujets = this.sujets.filter(sujet => sujet.id !== id);
    }
  }
}
