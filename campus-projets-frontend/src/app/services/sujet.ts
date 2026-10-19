
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sujet, SujetRequest } from '../models/sujet';

@Injectable({
  providedIn: 'root'
})
export class SujetService {
  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/sujets';

  listerDisponibles(): Observable<Sujet[]> {
    return this.http.get<Sujet[]>(this.apiUrl);
  }

  listerParEnseignant(enseignantId: number): Observable<Sujet[]> {
    return this.http.get<Sujet[]>(
      `${this.apiUrl}/enseignant/${enseignantId}`
    );
  }

  consulter(id: number): Observable<Sujet> {
    return this.http.get<Sujet>(`${this.apiUrl}/${id}`);
  }

  creer(request: SujetRequest): Observable<Sujet> {
    return this.http.post<Sujet>(this.apiUrl, request);
  }

  modifier(id: number, request: SujetRequest): Observable<Sujet> {
    return this.http.put<Sujet>(`${this.apiUrl}/${id}`, request);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
