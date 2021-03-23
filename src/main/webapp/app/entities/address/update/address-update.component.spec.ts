jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AddressService } from '../service/address.service';
import { IAddress, Address } from '../address.model';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

import { AddressUpdateComponent } from './address-update.component';

describe('Component Tests', () => {
  describe('Address Management Update Component', () => {
    let comp: AddressUpdateComponent;
    let fixture: ComponentFixture<AddressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let addressService: AddressService;
    let countryService: CountryService;
    let applicantService: ApplicantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AddressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AddressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AddressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      addressService = TestBed.inject(AddressService);
      countryService = TestBed.inject(CountryService);
      applicantService = TestBed.inject(ApplicantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call country query and add missing value', () => {
        const address: IAddress = { id: 456 };
        const country: ICountry = { id: 90166 };
        address.country = country;

        const countryCollection: ICountry[] = [{ id: 23491 }];
        spyOn(countryService, 'query').and.returnValue(of(new HttpResponse({ body: countryCollection })));
        const expectedCollection: ICountry[] = [country, ...countryCollection];
        spyOn(countryService, 'addCountryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ address });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryCollection, country);
        expect(comp.countriesCollection).toEqual(expectedCollection);
      });

      it('Should call countryOfBirth query and add missing value', () => {
        const address: IAddress = { id: 456 };
        const countryOfBirth: ICountry = { id: 96285 };
        address.countryOfBirth = countryOfBirth;

        const countryOfBirthCollection: ICountry[] = [{ id: 68721 }];
        spyOn(countryService, 'query').and.returnValue(of(new HttpResponse({ body: countryOfBirthCollection })));
        const expectedCollection: ICountry[] = [countryOfBirth, ...countryOfBirthCollection];
        spyOn(countryService, 'addCountryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ address });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryOfBirthCollection, countryOfBirth);
        expect(comp.countryOfBirthsCollection).toEqual(expectedCollection);
      });

      it('Should call countryOfResidence query and add missing value', () => {
        const address: IAddress = { id: 456 };
        const countryOfResidence: ICountry = { id: 76591 };
        address.countryOfResidence = countryOfResidence;

        const countryOfResidenceCollection: ICountry[] = [{ id: 11851 }];
        spyOn(countryService, 'query').and.returnValue(of(new HttpResponse({ body: countryOfResidenceCollection })));
        const expectedCollection: ICountry[] = [countryOfResidence, ...countryOfResidenceCollection];
        spyOn(countryService, 'addCountryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ address });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryOfResidenceCollection, countryOfResidence);
        expect(comp.countryOfResidencesCollection).toEqual(expectedCollection);
      });

      it('Should call Applicant query and add missing value', () => {
        const address: IAddress = { id: 456 };
        const applicant: IApplicant = { id: 95165 };
        address.applicant = applicant;

        const applicantCollection: IApplicant[] = [{ id: 84543 }];
        spyOn(applicantService, 'query').and.returnValue(of(new HttpResponse({ body: applicantCollection })));
        const additionalApplicants = [applicant];
        const expectedCollection: IApplicant[] = [...additionalApplicants, ...applicantCollection];
        spyOn(applicantService, 'addApplicantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ address });
        comp.ngOnInit();

        expect(applicantService.query).toHaveBeenCalled();
        expect(applicantService.addApplicantToCollectionIfMissing).toHaveBeenCalledWith(applicantCollection, ...additionalApplicants);
        expect(comp.applicantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const address: IAddress = { id: 456 };
        const country: ICountry = { id: 12672 };
        address.country = country;
        const countryOfBirth: ICountry = { id: 60511 };
        address.countryOfBirth = countryOfBirth;
        const countryOfResidence: ICountry = { id: 56813 };
        address.countryOfResidence = countryOfResidence;
        const applicant: IApplicant = { id: 30332 };
        address.applicant = applicant;

        activatedRoute.data = of({ address });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(address));
        expect(comp.countriesCollection).toContain(country);
        expect(comp.countryOfBirthsCollection).toContain(countryOfBirth);
        expect(comp.countryOfResidencesCollection).toContain(countryOfResidence);
        expect(comp.applicantsSharedCollection).toContain(applicant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const address = { id: 123 };
        spyOn(addressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ address });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: address }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(addressService.update).toHaveBeenCalledWith(address);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const address = new Address();
        spyOn(addressService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ address });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: address }));
        saveSubject.complete();

        // THEN
        expect(addressService.create).toHaveBeenCalledWith(address);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const address = { id: 123 };
        spyOn(addressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ address });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(addressService.update).toHaveBeenCalledWith(address);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCountryById', () => {
        it('Should return tracked Country primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCountryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
