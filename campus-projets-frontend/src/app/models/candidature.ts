
export type StatutCandidature =
  | 'EN_ATTENTE'
  | 'ACCEPTEE'
  | 'REJETEE';

export interface Candidature {
  id: number;
  groupeId: number;
  sujetId: number;
  ordrePreference: number;
  statut: StatutCandidature;
}
