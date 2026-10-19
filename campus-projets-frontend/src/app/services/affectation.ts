
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Affectation } from '../models/affectation';

@Injectable({
  providedIn: 'root'
})
export class AffectationService {
  private readonly apiUrl = 'http://localhost:8080/api/affectations';

  constructor(private http: HttpClient) {}

  listerToutes(): Observable<Affectation[]> {
    return this.http.get<Affectation[]>(this.apiUrl);
  }

  listerParResponsable(responsableId: number): Observable<Affectation[]> {
    return this.http.get<Affectation[]>(
      `${this.apiUrl}/responsable/${responsableId}`
    );
  }
}
