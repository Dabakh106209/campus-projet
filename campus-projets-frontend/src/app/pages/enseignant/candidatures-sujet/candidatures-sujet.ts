
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Candidature } from '../../../models/candidature';
import { CandidatureService } from '../../../services/candidature';

@Component({
  selector: 'app-candidatures-sujet',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './candidatures-sujet.html',
  styleUrl: './candidatures-sujet.scss',
})
export class CandidaturesSujet implements OnInit {
  candidatures: Candidature[] = [];
  chargement = false;
  erreur = '';
  sujetId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private candidatureService: CandidatureService,
      private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id || !Number.isInteger(Number(id)) || Number(id) <= 0) {
      this.erreur = 'Identifiant du sujet invalide.';
      return;
    }

    this.sujetId = Number(id);
    this.chargerCandidatures();
  }
 
chargerCandidatures(): void {
  if (this.sujetId === null) return;

  this.chargement = true;
  this.erreur = '';
  this.cdr.detectChanges();

  this.candidatureService.listerParSujet(this.sujetId).subscribe({
    next: (donnees) => {
      console.log('Candidatures reçues :', donnees);

      this.candidatures = donnees;
      this.chargement = false;

      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error('Erreur de chargement :', err);

      this.erreur =
        `Impossible de charger les candidatures (HTTP ${err.status}).`;
      this.chargement = false;

      this.cdr.detectChanges();
    }
  });
}

}
