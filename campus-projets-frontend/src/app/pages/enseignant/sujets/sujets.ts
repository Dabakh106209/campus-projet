
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Sujet } from '../../../models/sujet';
import { SujetService } from '../../../services/sujet';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sujets',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sujets.html',
  styleUrl: './sujets.scss',
})
export class Sujets implements OnInit {
  searchText = '';
  sujets: Sujet[] = [];
  chargement = false;
  erreur = '';

  // Identifiant temporaire pour nos tests.
  private readonly enseignantId = 2;

  constructor(private sujetService: SujetService, private router: Router, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.chargerSujets();
  }

 
chargerSujets(): void {
  this.chargement = true;
  this.erreur = '';

  this.sujetService.listerParEnseignant(this.enseignantId).subscribe({
    next: (donnees) => {
      console.log('Sujets reçus :', donnees);

      this.sujets = [...donnees];
      this.chargement = false;

      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error('Erreur de chargement :', err);
      this.erreur = 'Impossible de charger les sujets.';
      this.chargement = false;

      this.cdr.detectChanges();
    }
  });
}


  get sujetsFiltres(): Sujet[] {
    const recherche = this.searchText.toLowerCase().trim();

    return this.sujets.filter(
      sujet =>
        sujet.titre.toLowerCase().includes(recherche) ||
        (sujet.domaine ?? '').toLowerCase().includes(recherche)
    );
  }

get sujetsDisponibles(): number {
  return this.sujets.filter(s => s.statut === 'DISPONIBLE').length;
}

get sujetsIndisponibles(): number {
  return this.sujets.filter(s => s.statut === 'INDISPONIBLE').length;
}

get capaciteTotale(): number {
  return this.sujets.reduce((total, s) => total + s.capaciteMax, 0);
}

  
proposerSujet(): void {
  this.router.navigate(['/enseignant/sujets/nouveau']);
}

consulterSujet(id: number): void {
  console.log('Consulter le sujet :', id);
}

modifierSujet(id: number): void {
  this.router.navigate(['/enseignant/sujets', id, 'modifier']);
}


supprimerSujet(id: number): void {
  const confirmation = confirm(
    'Voulez-vous vraiment supprimer ce sujet ?'
  );

  if (!confirmation) {
    return;
  }

  this.sujetService.supprimer(id).subscribe({
    next: () => {
      // Met à jour immédiatement la liste affichée.
      this.sujets = this.sujets.filter(
        sujet => sujet.id !== id
      );

      console.log('Sujet supprimé :', id);

      // Recharge les données depuis le backend.
      this.chargerSujets();
    },
    error: (err) => {
      console.error('Erreur lors de la suppression :', err);
      alert(
        'Impossible de supprimer le sujet. Vérifiez le backend.'
      );
    }
  });
}


voirCandidatures(id: number): void {
  this.router.navigate(['/enseignant/sujets', id, 'candidatures']);
}

}
