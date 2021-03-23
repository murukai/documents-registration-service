import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppointmentSettingsDetailComponent } from './appointment-settings-detail.component';

describe('Component Tests', () => {
  describe('AppointmentSettings Management Detail Component', () => {
    let comp: AppointmentSettingsDetailComponent;
    let fixture: ComponentFixture<AppointmentSettingsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AppointmentSettingsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ appointmentSettings: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AppointmentSettingsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppointmentSettingsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appointmentSettings on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appointmentSettings).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
