import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnseignantLayoutComponent } from './enseignant-layout';

describe('EnseignantLayout', () => {
  let component: EnseignantLayoutComponent;
  let fixture: ComponentFixture<EnseignantLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnseignantLayoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EnseignantLayoutComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
