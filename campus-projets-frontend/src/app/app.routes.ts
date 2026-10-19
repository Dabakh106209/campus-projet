
import { Routes } from '@angular/router';

import { EnseignantLayoutComponent } from './layout/enseignant-layout/enseignant-layout';
import { Sujets } from './pages/enseignant/sujets/sujets';
import { SujetForm } from './pages/enseignant/sujet-form/sujet-form';
import { CandidaturesSujet } from './pages/enseignant/candidatures-sujet/candidatures-sujet';
import { Candidatures } from './pages/responsable/candidatures/candidatures';
import { ResponsableLayoutComponent } from './layout/responsable-layout/responsable-layout';
import { Affectation } from './pages/responsable/affectation/affectation';


export const routes: Routes = [
  {
    path: 'enseignant',
    component: EnseignantLayoutComponent,
    children: [
      {
        path: 'sujets',
        component: Sujets
      },
      { path: 'sujets/nouveau',
         component: SujetForm },
         {
  path: 'sujets/:id/modifier',
  component: SujetForm
},
{
  path: 'sujets/:id/candidatures',
  component: CandidaturesSujet
},
      {
        path: '',
        redirectTo: 'sujets',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    redirectTo: 'enseignant/sujets',
    pathMatch: 'full'
  },
  {
  path: 'responsable',
  component: ResponsableLayoutComponent,
  children: [
    {
      path: 'candidatures',
      component: Candidatures
    },
     {
      path: 'affectations',
      component: Affectation
    },
     
    {
      path: '',
      redirectTo: 'candidatures',
      pathMatch: 'full'
    }
  ]
},
  {
    path: '**',
    redirectTo: 'enseignant/sujets'
  }

];
