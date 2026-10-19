import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { Sidebar } from '../sidebar/sidebar';
import { Header } from '../header/header';

@Component({
  selector: 'app-enseignant-layout',
  standalone: true,
  imports: [
    Sidebar,
    Header,
    RouterOutlet
  ],
  templateUrl: './enseignant-layout.html',
  styleUrl: './enseignant-layout.scss'
})
export class EnseignantLayoutComponent {}