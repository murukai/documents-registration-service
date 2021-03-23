jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NextOfKeenService } from '../service/next-of-keen.service';
import { INextOfKeen, NextOfKeen } from '../next-of-keen.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

import { NextOfKeenUpdateComponent } from './next-of-keen-update.component';

describe('Component Tests', () => {
  describe('NextOfKeen Management Update Component', () => {
    let comp: NextOfKeenUpdateComponent;
    let fixture: ComponentFixture<NextOfKeenUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let nextOfKeenService: NextOfKeenService;
    let applicantService: ApplicantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NextOfKeenUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NextOfKeenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NextOfKeenUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      nextOfKeenService = TestBed.inject(NextOfKeenService);
      applicantService = TestBed.inject(ApplicantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call applicant query and add missing value', () => {
        const nextOfKeen: INextOfKeen = { id: 456 };
        const applicant: IApplicant = { id: 81802 };
        nextOfKeen.applicant = applicant;

        const applicantCollection: IApplicant[] = [{ id: 9029 }];
        spyOn(applicantService, 'query').and.returnValue(of(new HttpResponse({ body: applicantCollection })));
        const expectedCollection: IApplicant[] = [applicant, ...applicantCollection];
        spyOn(applicantService, 'addApplicantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ nextOfKeen });
        comp.ngOnInit();

        expect(applicantService.query).toHaveBeenCalled();
        expect(applicantService.addApplicantToCollectionIfMissing).toHaveBeenCalledWith(applicantCollection, applicant);
        expect(comp.applicantsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const nextOfKeen: INextOfKeen = { id: 456 };
        const applicant: IApplicant = { id: 58030 };
        nextOfKeen.applicant = applicant;

        activatedRoute.data = of({ nextOfKeen });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(nextOfKeen));
        expect(comp.applicantsCollection).toContain(applicant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nextOfKeen = { id: 123 };
        spyOn(nextOfKeenService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nextOfKeen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nextOfKeen }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(nextOfKeenService.update).toHaveBeenCalledWith(nextOfKeen);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nextOfKeen = new NextOfKeen();
        spyOn(nextOfKeenService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nextOfKeen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nextOfKeen }));
        saveSubject.complete();

        // THEN
        expect(nextOfKeenService.create).toHaveBeenCalledWith(nextOfKeen);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nextOfKeen = { id: 123 };
        spyOn(nextOfKeenService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nextOfKeen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(nextOfKeenService.update).toHaveBeenCalledWith(nextOfKeen);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApplicantById', () => {
        it('Should return tracked Applicant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplicantById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
