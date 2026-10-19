import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
    @Input() role: 'enseignant' | 'responsable' = 'enseignant';
   
    @Input() pageTitre = 'Mes sujets';
  searchText = '';
}