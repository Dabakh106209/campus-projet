import { TestBed } from '@angular/core/testing';

import { Sujet } from './sujet';

describe('Sujet', () => {
  let service: Sujet;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Sujet);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
