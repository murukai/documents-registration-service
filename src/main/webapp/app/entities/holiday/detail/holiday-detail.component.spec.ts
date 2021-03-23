import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HolidayDetailComponent } from './holiday-detail.component';

describe('Component Tests', () => {
  describe('Holiday Management Detail Component', () => {
    let comp: HolidayDetailComponent;
    let fixture: ComponentFixture<HolidayDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [HolidayDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ holiday: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(HolidayDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HolidayDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load holiday on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.holiday).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
