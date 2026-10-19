
import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: 'enseignant/sujets/:id/modifier',
    renderMode: RenderMode.Client
  },
  {
    path: 'enseignant/sujets/:id/candidatures',
    renderMode: RenderMode.Client
  },
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
