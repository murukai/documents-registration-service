jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppointmentSettingsService } from '../service/appointment-settings.service';
import { IAppointmentSettings, AppointmentSettings } from '../appointment-settings.model';

import { AppointmentSettingsUpdateComponent } from './appointment-settings-update.component';

describe('Component Tests', () => {
  describe('AppointmentSettings Management Update Component', () => {
    let comp: AppointmentSettingsUpdateComponent;
    let fixture: ComponentFixture<AppointmentSettingsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appointmentSettingsService: AppointmentSettingsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentSettingsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppointmentSettingsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentSettingsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appointmentSettingsService = TestBed.inject(AppointmentSettingsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const appointmentSettings: IAppointmentSettings = { id: 456 };

        activatedRoute.data = of({ appointmentSettings });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appointmentSettings));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointmentSettings = { id: 123 };
        spyOn(appointmentSettingsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSettings });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentSettings }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appointmentSettingsService.update).toHaveBeenCalledWith(appointmentSettings);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointmentSettings = new AppointmentSettings();
        spyOn(appointmentSettingsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSettings });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentSettings }));
        saveSubject.complete();

        // THEN
        expect(appointmentSettingsService.create).toHaveBeenCalledWith(appointmentSettings);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointmentSettings = { id: 123 };
        spyOn(appointmentSettingsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSettings });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appointmentSettingsService.update).toHaveBeenCalledWith(appointmentSettings);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
