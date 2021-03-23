import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { EyeColor } from 'app/entities/enumerations/eye-color.model';
import { HairColor } from 'app/entities/enumerations/hair-color.model';
import { IApplicant, Applicant } from '../applicant.model';

import { ApplicantService } from './applicant.service';

describe('Service Tests', () => {
  describe('Applicant Service', () => {
    let service: ApplicantService;
    let httpMock: HttpTestingController;
    let elemDefault: IApplicant;
    let expectedResult: IApplicant | IApplicant[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ApplicantService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        firstNames: 'AAAAAAA',
        lastName: 'AAAAAAA',
        initials: 'AAAAAAA',
        gender: Gender.MALE,
        email: 'AAAAAAA',
        maritalStatus: MaritalStatus.MARRIED,
        dateOfBirth: currentDate,
        idNumber: 'AAAAAAA',
        birthEntryNumber: 'AAAAAAA',
        eyeColor: EyeColor.BROWN,
        hairColor: HairColor.BLACK,
        visibleMarks: 'AAAAAAA',
        profession: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Applicant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.create(new Applicant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Applicant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstNames: 'BBBBBB',
            lastName: 'BBBBBB',
            initials: 'BBBBBB',
            gender: 'BBBBBB',
            email: 'BBBBBB',
            maritalStatus: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
            idNumber: 'BBBBBB',
            birthEntryNumber: 'BBBBBB',
            eyeColor: 'BBBBBB',
            hairColor: 'BBBBBB',
            visibleMarks: 'BBBBBB',
            profession: 'BBBBBB',
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Applicant', () => {
        const patchObject = Object.assign(
          {
            lastName: 'BBBBBB',
            initials: 'BBBBBB',
            email: 'BBBBBB',
            maritalStatus: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
            birthEntryNumber: 'BBBBBB',
            hairColor: 'BBBBBB',
            visibleMarks: 'BBBBBB',
          },
          new Applicant()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Applicant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstNames: 'BBBBBB',
            lastName: 'BBBBBB',
            initials: 'BBBBBB',
            gender: 'BBBBBB',
            email: 'BBBBBB',
            maritalStatus: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
            idNumber: 'BBBBBB',
            birthEntryNumber: 'BBBBBB',
            eyeColor: 'BBBBBB',
            hairColor: 'BBBBBB',
            visibleMarks: 'BBBBBB',
            profession: 'BBBBBB',
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Applicant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addApplicantToCollectionIfMissing', () => {
        it('should add a Applicant to an empty array', () => {
          const applicant: IApplicant = { id: 123 };
          expectedResult = service.addApplicantToCollectionIfMissing([], applicant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(applicant);
        });

        it('should not add a Applicant to an array that contains it', () => {
          const applicant: IApplicant = { id: 123 };
          const applicantCollection: IApplicant[] = [
            {
              ...applicant,
            },
            { id: 456 },
          ];
          expectedResult = service.addApplicantToCollectionIfMissing(applicantCollection, applicant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Applicant to an array that doesn't contain it", () => {
          const applicant: IApplicant = { id: 123 };
          const applicantCollection: IApplicant[] = [{ id: 456 }];
          expectedResult = service.addApplicantToCollectionIfMissing(applicantCollection, applicant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(applicant);
        });

        it('should add only unique Applicant to an array', () => {
          const applicantArray: IApplicant[] = [{ id: 123 }, { id: 456 }, { id: 27213 }];
          const applicantCollection: IApplicant[] = [{ id: 123 }];
          expectedResult = service.addApplicantToCollectionIfMissing(applicantCollection, ...applicantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const applicant: IApplicant = { id: 123 };
          const applicant2: IApplicant = { id: 456 };
          expectedResult = service.addApplicantToCollectionIfMissing([], applicant, applicant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(applicant);
          expect(expectedResult).toContain(applicant2);
        });

        it('should accept null and undefined values', () => {
          const applicant: IApplicant = { id: 123 };
          expectedResult = service.addApplicantToCollectionIfMissing([], null, applicant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(applicant);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
