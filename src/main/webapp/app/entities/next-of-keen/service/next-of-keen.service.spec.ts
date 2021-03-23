import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INextOfKeen, NextOfKeen } from '../next-of-keen.model';

import { NextOfKeenService } from './next-of-keen.service';

describe('Service Tests', () => {
  describe('NextOfKeen Service', () => {
    let service: NextOfKeenService;
    let httpMock: HttpTestingController;
    let elemDefault: INextOfKeen;
    let expectedResult: INextOfKeen | INextOfKeen[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NextOfKeenService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fullName: 'AAAAAAA',
        address: 'AAAAAAA',
        cellphone: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a NextOfKeen', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new NextOfKeen()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a NextOfKeen', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            address: 'BBBBBB',
            cellphone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a NextOfKeen', () => {
        const patchObject = Object.assign(
          {
            address: 'BBBBBB',
          },
          new NextOfKeen()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of NextOfKeen', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            address: 'BBBBBB',
            cellphone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a NextOfKeen', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNextOfKeenToCollectionIfMissing', () => {
        it('should add a NextOfKeen to an empty array', () => {
          const nextOfKeen: INextOfKeen = { id: 123 };
          expectedResult = service.addNextOfKeenToCollectionIfMissing([], nextOfKeen);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nextOfKeen);
        });

        it('should not add a NextOfKeen to an array that contains it', () => {
          const nextOfKeen: INextOfKeen = { id: 123 };
          const nextOfKeenCollection: INextOfKeen[] = [
            {
              ...nextOfKeen,
            },
            { id: 456 },
          ];
          expectedResult = service.addNextOfKeenToCollectionIfMissing(nextOfKeenCollection, nextOfKeen);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a NextOfKeen to an array that doesn't contain it", () => {
          const nextOfKeen: INextOfKeen = { id: 123 };
          const nextOfKeenCollection: INextOfKeen[] = [{ id: 456 }];
          expectedResult = service.addNextOfKeenToCollectionIfMissing(nextOfKeenCollection, nextOfKeen);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nextOfKeen);
        });

        it('should add only unique NextOfKeen to an array', () => {
          const nextOfKeenArray: INextOfKeen[] = [{ id: 123 }, { id: 456 }, { id: 14363 }];
          const nextOfKeenCollection: INextOfKeen[] = [{ id: 123 }];
          expectedResult = service.addNextOfKeenToCollectionIfMissing(nextOfKeenCollection, ...nextOfKeenArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const nextOfKeen: INextOfKeen = { id: 123 };
          const nextOfKeen2: INextOfKeen = { id: 456 };
          expectedResult = service.addNextOfKeenToCollectionIfMissing([], nextOfKeen, nextOfKeen2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nextOfKeen);
          expect(expectedResult).toContain(nextOfKeen2);
        });

        it('should accept null and undefined values', () => {
          const nextOfKeen: INextOfKeen = { id: 123 };
          expectedResult = service.addNextOfKeenToCollectionIfMissing([], null, nextOfKeen, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nextOfKeen);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
