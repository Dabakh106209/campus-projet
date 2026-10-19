import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandidaturesSujet } from './candidatures-sujet';

describe('CandidaturesSujet', () => {
  let component: CandidaturesSujet;
  let fixture: ComponentFixture<CandidaturesSujet>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidaturesSujet]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CandidaturesSujet);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
