jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplicantService } from '../service/applicant.service';
import { IApplicant, Applicant } from '../applicant.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { AppointmentSlotService } from 'app/entities/appointment-slot/service/appointment-slot.service';

import { ApplicantUpdateComponent } from './applicant-update.component';

describe('Component Tests', () => {
  describe('Applicant Management Update Component', () => {
    let comp: ApplicantUpdateComponent;
    let fixture: ComponentFixture<ApplicantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let applicantService: ApplicantService;
    let userService: UserService;
    let appointmentSlotService: AppointmentSlotService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApplicantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApplicantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      applicantService = TestBed.inject(ApplicantService);
      userService = TestBed.inject(UserService);
      appointmentSlotService = TestBed.inject(AppointmentSlotService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const applicant: IApplicant = { id: 456 };
        const user: IUser = { id: 63569 };
        applicant.user = user;

        const userCollection: IUser[] = [{ id: 56837 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AppointmentSlot query and add missing value', () => {
        const applicant: IApplicant = { id: 456 };
        const appointmentSlot: IAppointmentSlot = { id: 46183 };
        applicant.appointmentSlot = appointmentSlot;

        const appointmentSlotCollection: IAppointmentSlot[] = [{ id: 21029 }];
        spyOn(appointmentSlotService, 'query').and.returnValue(of(new HttpResponse({ body: appointmentSlotCollection })));
        const additionalAppointmentSlots = [appointmentSlot];
        const expectedCollection: IAppointmentSlot[] = [...additionalAppointmentSlots, ...appointmentSlotCollection];
        spyOn(appointmentSlotService, 'addAppointmentSlotToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(appointmentSlotService.query).toHaveBeenCalled();
        expect(appointmentSlotService.addAppointmentSlotToCollectionIfMissing).toHaveBeenCalledWith(
          appointmentSlotCollection,
          ...additionalAppointmentSlots
        );
        expect(comp.appointmentSlotsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const applicant: IApplicant = { id: 456 };
        const user: IUser = { id: 49393 };
        applicant.user = user;
        const appointmentSlot: IAppointmentSlot = { id: 41962 };
        applicant.appointmentSlot = appointmentSlot;

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(applicant));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.appointmentSlotsSharedCollection).toContain(appointmentSlot);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicant = { id: 123 };
        spyOn(applicantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(applicantService.update).toHaveBeenCalledWith(applicant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicant = new Applicant();
        spyOn(applicantService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicant }));
        saveSubject.complete();

        // THEN
        expect(applicantService.create).toHaveBeenCalledWith(applicant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicant = { id: 123 };
        spyOn(applicantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(applicantService.update).toHaveBeenCalledWith(applicant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAppointmentSlotById', () => {
        it('Should return tracked AppointmentSlot primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppointmentSlotById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
