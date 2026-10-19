
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Affectation as AffectationModel } from '../../../models/affectation';
import { AffectationService } from '../../../services/affectation';

@Component({
  selector: 'app-affectation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './affectation.html',
  styleUrl: './affectation.scss'
})
export class Affectation implements OnInit {
  affectations: AffectationModel[] = [];
  chargement = false;
  erreur = '';

  constructor(
    private affectationService: AffectationService, private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerAffectations();
  }

  chargerAffectations(): void {
    this.chargement = true;
    this.erreur = '';

    this.affectationService.listerToutes().subscribe({
      next: (donnees) => {
        this.affectations = donnees;
        this.chargement = false;
         this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur de chargement des affectations :', err);
        this.erreur = `Impossible de charger les affectations (HTTP ${err.status}).`;
        this.chargement = false;
      }
    });
  }
}
