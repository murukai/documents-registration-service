import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NextOfKeenDetailComponent } from './next-of-keen-detail.component';

describe('Component Tests', () => {
  describe('NextOfKeen Management Detail Component', () => {
    let comp: NextOfKeenDetailComponent;
    let fixture: ComponentFixture<NextOfKeenDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NextOfKeenDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ nextOfKeen: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NextOfKeenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NextOfKeenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load nextOfKeen on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nextOfKeen).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
