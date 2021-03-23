jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppointmentService } from '../service/appointment.service';
import { IAppointment, Appointment } from '../appointment.model';

import { AppointmentUpdateComponent } from './appointment-update.component';

describe('Component Tests', () => {
  describe('Appointment Management Update Component', () => {
    let comp: AppointmentUpdateComponent;
    let fixture: ComponentFixture<AppointmentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appointmentService: AppointmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppointmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appointmentService = TestBed.inject(AppointmentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const appointment: IAppointment = { id: 456 };

        activatedRoute.data = of({ appointment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appointment));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointment = { id: 123 };
        spyOn(appointmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appointmentService.update).toHaveBeenCalledWith(appointment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointment = new Appointment();
        spyOn(appointmentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointment }));
        saveSubject.complete();

        // THEN
        expect(appointmentService.create).toHaveBeenCalledWith(appointment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointment = { id: 123 };
        spyOn(appointmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appointmentService.update).toHaveBeenCalledWith(appointment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
