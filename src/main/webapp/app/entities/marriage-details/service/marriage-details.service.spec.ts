import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMarriageDetails, MarriageDetails } from '../marriage-details.model';

import { MarriageDetailsService } from './marriage-details.service';

describe('Service Tests', () => {
  describe('MarriageDetails Service', () => {
    let service: MarriageDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IMarriageDetails;
    let expectedResult: IMarriageDetails | IMarriageDetails[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MarriageDetailsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateOfMarriage: currentDate,
        spouseFullName: 'AAAAAAA',
        placeOfMarriage: 'AAAAAAA',
        spousePlaceOfBirth: 'AAAAAAA',
        marriageNumber: 'AAAAAAA',
        marriedBefore: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfMarriage: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MarriageDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfMarriage: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfMarriage: currentDate,
          },
          returnedFromService
        );

        service.create(new MarriageDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MarriageDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateOfMarriage: currentDate.format(DATE_FORMAT),
            spouseFullName: 'BBBBBB',
            placeOfMarriage: 'BBBBBB',
            spousePlaceOfBirth: 'BBBBBB',
            marriageNumber: 'BBBBBB',
            marriedBefore: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfMarriage: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MarriageDetails', () => {
        const patchObject = Object.assign(
          {
            marriedBefore: true,
          },
          new MarriageDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateOfMarriage: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MarriageDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateOfMarriage: currentDate.format(DATE_FORMAT),
            spouseFullName: 'BBBBBB',
            placeOfMarriage: 'BBBBBB',
            spousePlaceOfBirth: 'BBBBBB',
            marriageNumber: 'BBBBBB',
            marriedBefore: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfMarriage: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MarriageDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMarriageDetailsToCollectionIfMissing', () => {
        it('should add a MarriageDetails to an empty array', () => {
          const marriageDetails: IMarriageDetails = { id: 123 };
          expectedResult = service.addMarriageDetailsToCollectionIfMissing([], marriageDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(marriageDetails);
        });

        it('should not add a MarriageDetails to an array that contains it', () => {
          const marriageDetails: IMarriageDetails = { id: 123 };
          const marriageDetailsCollection: IMarriageDetails[] = [
            {
              ...marriageDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addMarriageDetailsToCollectionIfMissing(marriageDetailsCollection, marriageDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MarriageDetails to an array that doesn't contain it", () => {
          const marriageDetails: IMarriageDetails = { id: 123 };
          const marriageDetailsCollection: IMarriageDetails[] = [{ id: 456 }];
          expectedResult = service.addMarriageDetailsToCollectionIfMissing(marriageDetailsCollection, marriageDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(marriageDetails);
        });

        it('should add only unique MarriageDetails to an array', () => {
          const marriageDetailsArray: IMarriageDetails[] = [{ id: 123 }, { id: 456 }, { id: 15941 }];
          const marriageDetailsCollection: IMarriageDetails[] = [{ id: 123 }];
          expectedResult = service.addMarriageDetailsToCollectionIfMissing(marriageDetailsCollection, ...marriageDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const marriageDetails: IMarriageDetails = { id: 123 };
          const marriageDetails2: IMarriageDetails = { id: 456 };
          expectedResult = service.addMarriageDetailsToCollectionIfMissing([], marriageDetails, marriageDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(marriageDetails);
          expect(expectedResult).toContain(marriageDetails2);
        });

        it('should accept null and undefined values', () => {
          const marriageDetails: IMarriageDetails = { id: 123 };
          expectedResult = service.addMarriageDetailsToCollectionIfMissing([], null, marriageDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(marriageDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
