import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarriageDetailsDetailComponent } from './marriage-details-detail.component';

describe('Component Tests', () => {
  describe('MarriageDetails Management Detail Component', () => {
    let comp: MarriageDetailsDetailComponent;
    let fixture: ComponentFixture<MarriageDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MarriageDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ marriageDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MarriageDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MarriageDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load marriageDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.marriageDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
