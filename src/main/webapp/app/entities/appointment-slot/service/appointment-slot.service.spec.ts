import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAppointmentSlot, AppointmentSlot } from '../appointment-slot.model';

import { AppointmentSlotService } from './appointment-slot.service';

describe('Service Tests', () => {
  describe('AppointmentSlot Service', () => {
    let service: AppointmentSlotService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppointmentSlot;
    let expectedResult: IAppointmentSlot | IAppointmentSlot[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppointmentSlotService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        timeOfAppointment: currentDate,
        available: false,
        maxAppointments: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            timeOfAppointment: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AppointmentSlot', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            timeOfAppointment: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeOfAppointment: currentDate,
          },
          returnedFromService
        );

        service.create(new AppointmentSlot()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AppointmentSlot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            timeOfAppointment: currentDate.format(DATE_TIME_FORMAT),
            available: true,
            maxAppointments: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeOfAppointment: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AppointmentSlot', () => {
        const patchObject = Object.assign(
          {
            timeOfAppointment: currentDate.format(DATE_TIME_FORMAT),
          },
          new AppointmentSlot()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            timeOfAppointment: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AppointmentSlot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            timeOfAppointment: currentDate.format(DATE_TIME_FORMAT),
            available: true,
            maxAppointments: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeOfAppointment: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AppointmentSlot', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppointmentSlotToCollectionIfMissing', () => {
        it('should add a AppointmentSlot to an empty array', () => {
          const appointmentSlot: IAppointmentSlot = { id: 123 };
          expectedResult = service.addAppointmentSlotToCollectionIfMissing([], appointmentSlot);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentSlot);
        });

        it('should not add a AppointmentSlot to an array that contains it', () => {
          const appointmentSlot: IAppointmentSlot = { id: 123 };
          const appointmentSlotCollection: IAppointmentSlot[] = [
            {
              ...appointmentSlot,
            },
            { id: 456 },
          ];
          expectedResult = service.addAppointmentSlotToCollectionIfMissing(appointmentSlotCollection, appointmentSlot);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AppointmentSlot to an array that doesn't contain it", () => {
          const appointmentSlot: IAppointmentSlot = { id: 123 };
          const appointmentSlotCollection: IAppointmentSlot[] = [{ id: 456 }];
          expectedResult = service.addAppointmentSlotToCollectionIfMissing(appointmentSlotCollection, appointmentSlot);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentSlot);
        });

        it('should add only unique AppointmentSlot to an array', () => {
          const appointmentSlotArray: IAppointmentSlot[] = [{ id: 123 }, { id: 456 }, { id: 99807 }];
          const appointmentSlotCollection: IAppointmentSlot[] = [{ id: 123 }];
          expectedResult = service.addAppointmentSlotToCollectionIfMissing(appointmentSlotCollection, ...appointmentSlotArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appointmentSlot: IAppointmentSlot = { id: 123 };
          const appointmentSlot2: IAppointmentSlot = { id: 456 };
          expectedResult = service.addAppointmentSlotToCollectionIfMissing([], appointmentSlot, appointmentSlot2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentSlot);
          expect(expectedResult).toContain(appointmentSlot2);
        });

        it('should accept null and undefined values', () => {
          const appointmentSlot: IAppointmentSlot = { id: 123 };
          expectedResult = service.addAppointmentSlotToCollectionIfMissing([], null, appointmentSlot, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentSlot);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
