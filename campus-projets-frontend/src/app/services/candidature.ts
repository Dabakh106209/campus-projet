
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Candidature } from '../models/candidature';

@Injectable({
  providedIn: 'root'
})
export class CandidatureService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/candidatures';

  listerParSujet(sujetId: number): Observable<Candidature[]> {
    return this.http.get<Candidature[]>(
      `${this.apiUrl}/sujet/${sujetId}`
    );
  }

  
listerToutes(): Observable<Candidature[]> {
  return this.http.get<Candidature[]>(this.apiUrl);
}

modifierStatut(
  id: number,
  statut: 'EN_ATTENTE' | 'ACCEPTEE' | 'REJETEE'
): Observable<Candidature> {
  return this.http.patch<Candidature>(
    `${this.apiUrl}/${id}/statut`,
    { statut }
  );
}

}
