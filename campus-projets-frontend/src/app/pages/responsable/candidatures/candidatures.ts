
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Candidature } from '../../../models/candidature';
import { CandidatureService } from '../../../services/candidature';

@Component({
  selector: 'app-responsable-candidatures',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './candidatures.html',
  styleUrl: './candidatures.scss'
})
export class Candidatures implements OnInit {
  candidatures: Candidature[] = [];
  chargement = false;
  erreur = '';
  message = '';
  candidatureEnCours: number | null = null;

  constructor(
    private candidatureService: CandidatureService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerCandidatures();
  }

  chargerCandidatures(): void {
    this.chargement = true;
    this.erreur = '';

    this.candidatureService.listerToutes().subscribe({
      next: (donnees) => {
        this.candidatures = donnees;
        this.chargement = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur de chargement :', err);
        this.erreur = `Impossible de charger les candidatures (HTTP ${err.status}).`;
        this.chargement = false;
        this.cdr.detectChanges();
      }
    });
  }

  changerStatut(
    candidature: Candidature,
    statut: 'ACCEPTEE' | 'REJETEE'
  ): void {
    const action = statut === 'ACCEPTEE' ? 'accepter' : 'rejeter';

    if (!confirm(`Voulez-vous vraiment ${action} cette candidature ?`)) {
      return;
    }

    this.candidatureEnCours = candidature.id;
    this.message = '';
    this.erreur = '';

    this.candidatureService.modifierStatut(candidature.id, statut).subscribe({
      next: (miseAJour) => {
        this.candidatures = this.candidatures.map(c =>
          c.id === miseAJour.id ? miseAJour : c
        );
        this.message = `La candidature #${miseAJour.id} a été mise à jour.`;
        this.candidatureEnCours = null;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur de mise à jour :', err);
        this.erreur = `Impossible de modifier le statut (HTTP ${err.status}).`;
        this.candidatureEnCours = null;
        this.cdr.detectChanges();
      }
    });
  }
}
