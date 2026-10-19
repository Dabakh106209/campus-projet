
export type StatutSujet = 'DISPONIBLE' | 'INDISPONIBLE' | 'ARCHIVE';

export interface Sujet {
  id: number;
  titre: string;
  description: string;
  domaine: string | null;
  capaciteMax: number;
  statut: StatutSujet;
  enseignantId: number;
  campagneId: number;
}

export interface SujetRequest {
  titre: string;
  description: string;
  domaine: string | null;
  capaciteMax: number;
  statut: StatutSujet;
  enseignantId: number;
  campagneId: number;
}
