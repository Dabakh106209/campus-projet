
import { Component, OnInit, ChangeDetectorRef} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { SujetService } from '../../../services/sujet';
import { SujetRequest } from '../../../models/sujet';


interface NouveauSujet {
  titre: string;
  description: string;
  categorie: string;
  capacite: number;
  competences: string;
  dateLimite: string;
}

@Component({
  selector: 'app-sujet-form',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './sujet-form.html',
  styleUrl: './sujet-form.scss',
})
export class SujetForm implements OnInit {
  dateMinimale = new Date().toISOString().split('T')[0];

  enregistrementEnCours = false;
  modeModification = false;
  idSujet: number | null = null;
  chargementSujet = false;
  erreur = '';
  statutSujet: 'DISPONIBLE' | 'INDISPONIBLE' | 'ARCHIVE' = 'DISPONIBLE';

  sujet: NouveauSujet = {
    titre: '',
    description: '',
    categorie: '',
    capacite: 1,
    competences: '',
    dateLimite: '',
  };

  // Identifiants temporaires pour les tests.
  private readonly enseignantId = 2;
  private readonly campagneId = 1;

  constructor(
    private router: Router,
    private sujetService: SujetService,
     private route: ActivatedRoute,
     private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
  const id = this.route.snapshot.paramMap.get('id');

  if (id !== null) {
    this.modeModification = true;
    this.idSujet = Number(id);
    this.chargerSujet(this.idSujet);
  }
}


chargerSujet(id: number): void {
  this.chargementSujet = true;
  this.erreur = '';
  

  console.log('Chargement du sujet, ID :', id);

  this.sujetService.consulter(id).subscribe({
  next: (sujetExistant) => {
  console.log('Sujet reçu du backend :', sujetExistant);

  this.sujet.titre = sujetExistant.titre;
  this.sujet.description = sujetExistant.description;
  this.sujet.categorie = sujetExistant.domaine ?? '';
  this.sujet.capacite = sujetExistant.capaciteMax;
  this.statutSujet = sujetExistant.statut;

  this.chargementSujet = false;

  this.cdr.detectChanges();
  console.log('Domaine reçu :', sujetExistant.domaine);
console.log('Sujet complet :', sujetExistant);
},
    error: (err) => {
      console.error('Erreur HTTP lors du chargement :', err);

      this.erreur = `Impossible de charger le sujet. Erreur HTTP : ${err.status}`;
      this.chargementSujet = false;
    }
  });

  
}

  
enregistrerSujet(form: NgForm): void {
  if (form.invalid || this.enregistrementEnCours) {
    form.control.markAllAsTouched();
    return;
  }

  this.erreur = '';
  this.enregistrementEnCours = true;

  const demande: SujetRequest = {
    titre: this.sujet.titre.trim(),
    description: this.sujet.description.trim(),
    domaine: this.sujet.categorie || null,
    capaciteMax: Number(this.sujet.capacite),
    statut: this.statutSujet,
    enseignantId: this.enseignantId,
    campagneId: this.campagneId,
  };

  // Modification si un identifiant existe, sinon création.
  const requete =
    this.modeModification && this.idSujet !== null
      ? this.sujetService.modifier(this.idSujet, demande)
      : this.sujetService.creer(demande);

  requete.subscribe({
    next: (resultat) => {
      console.log('Sujet enregistré avec succès :', resultat);
      this.enregistrementEnCours = false;

      this.router.navigate(['/enseignant/sujets']).then(
        (navigationReussie) => {
          console.log('Navigation réussie :', navigationReussie);
        }
      );
    },
    error: (err) => {
      console.error('Erreur lors de l’enregistrement :', err);
      this.enregistrementEnCours = false;

      if (err.status === 0) {
        this.erreur =
          'Le serveur est inaccessible. Vérifiez que Spring Boot fonctionne.';
      } else if (err.status === 400) {
        this.erreur =
          'Les données sont invalides. Vérifiez les informations envoyées.';
      } else {
        this.erreur =
          `Une erreur est survenue (HTTP ${err.status}).`;
      }
    },
  });
}

}
