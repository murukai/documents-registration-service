jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppointmentSlotService } from '../service/appointment-slot.service';
import { IAppointmentSlot, AppointmentSlot } from '../appointment-slot.model';
import { IAppointment } from 'app/entities/appointment/appointment.model';
import { AppointmentService } from 'app/entities/appointment/service/appointment.service';

import { AppointmentSlotUpdateComponent } from './appointment-slot-update.component';

describe('Component Tests', () => {
  describe('AppointmentSlot Management Update Component', () => {
    let comp: AppointmentSlotUpdateComponent;
    let fixture: ComponentFixture<AppointmentSlotUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appointmentSlotService: AppointmentSlotService;
    let appointmentService: AppointmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentSlotUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppointmentSlotUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentSlotUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appointmentSlotService = TestBed.inject(AppointmentSlotService);
      appointmentService = TestBed.inject(AppointmentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appointment query and add missing value', () => {
        const appointmentSlot: IAppointmentSlot = { id: 456 };
        const appointment: IAppointment = { id: 50810 };
        appointmentSlot.appointment = appointment;

        const appointmentCollection: IAppointment[] = [{ id: 71171 }];
        spyOn(appointmentService, 'query').and.returnValue(of(new HttpResponse({ body: appointmentCollection })));
        const additionalAppointments = [appointment];
        const expectedCollection: IAppointment[] = [...additionalAppointments, ...appointmentCollection];
        spyOn(appointmentService, 'addAppointmentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        expect(appointmentService.query).toHaveBeenCalled();
        expect(appointmentService.addAppointmentToCollectionIfMissing).toHaveBeenCalledWith(
          appointmentCollection,
          ...additionalAppointments
        );
        expect(comp.appointmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const appointmentSlot: IAppointmentSlot = { id: 456 };
        const appointment: IAppointment = { id: 75536 };
        appointmentSlot.appointment = appointment;

        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appointmentSlot));
        expect(comp.appointmentsSharedCollection).toContain(appointment);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointmentSlot = { id: 123 };
        spyOn(appointmentSlotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentSlot }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appointmentSlotService.update).toHaveBeenCalledWith(appointmentSlot);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointmentSlot = new AppointmentSlot();
        spyOn(appointmentSlotService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentSlot }));
        saveSubject.complete();

        // THEN
        expect(appointmentSlotService.create).toHaveBeenCalledWith(appointmentSlot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const appointmentSlot = { id: 123 };
        spyOn(appointmentSlotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appointmentSlotService.update).toHaveBeenCalledWith(appointmentSlot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppointmentById', () => {
        it('Should return tracked Appointment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppointmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
