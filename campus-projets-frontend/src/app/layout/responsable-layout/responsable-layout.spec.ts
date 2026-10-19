import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponsableLayoutComponent } from './responsable-layout';

describe('ResponsableLayout', () => {
  let component: ResponsableLayoutComponent;
  let fixture: ComponentFixture<ResponsableLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponsableLayoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponsableLayoutComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
