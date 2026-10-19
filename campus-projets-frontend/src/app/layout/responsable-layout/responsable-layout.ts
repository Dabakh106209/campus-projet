

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../sidebar/sidebar';
import { Header } from '../header/header';

@Component({
  selector: 'app-responsable-layout',
  standalone: true,
  imports: [RouterOutlet, Sidebar, Header],
  templateUrl: './responsable-layout.html',
  styleUrl: './responsable-layout.scss'
})
export class ResponsableLayoutComponent {}
