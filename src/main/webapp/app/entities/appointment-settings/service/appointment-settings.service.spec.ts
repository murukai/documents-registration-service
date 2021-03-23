import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppointmentSettings, AppointmentSettings } from '../appointment-settings.model';

import { AppointmentSettingsService } from './appointment-settings.service';

describe('Service Tests', () => {
  describe('AppointmentSettings Service', () => {
    let service: AppointmentSettingsService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppointmentSettings;
    let expectedResult: IAppointmentSettings | IAppointmentSettings[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppointmentSettingsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        maxOrdinaryAppointments: 0,
        maxEmergencyAppointments: 0,
        startingWorkTime: 0,
        endingWorkTime: 0,
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

      it('should create a AppointmentSettings', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AppointmentSettings()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AppointmentSettings', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            maxOrdinaryAppointments: 1,
            maxEmergencyAppointments: 1,
            startingWorkTime: 1,
            endingWorkTime: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AppointmentSettings', () => {
        const patchObject = Object.assign(
          {
            startingWorkTime: 1,
            endingWorkTime: 1,
          },
          new AppointmentSettings()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AppointmentSettings', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            maxOrdinaryAppointments: 1,
            maxEmergencyAppointments: 1,
            startingWorkTime: 1,
            endingWorkTime: 1,
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

      it('should delete a AppointmentSettings', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppointmentSettingsToCollectionIfMissing', () => {
        it('should add a AppointmentSettings to an empty array', () => {
          const appointmentSettings: IAppointmentSettings = { id: 123 };
          expectedResult = service.addAppointmentSettingsToCollectionIfMissing([], appointmentSettings);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentSettings);
        });

        it('should not add a AppointmentSettings to an array that contains it', () => {
          const appointmentSettings: IAppointmentSettings = { id: 123 };
          const appointmentSettingsCollection: IAppointmentSettings[] = [
            {
              ...appointmentSettings,
            },
            { id: 456 },
          ];
          expectedResult = service.addAppointmentSettingsToCollectionIfMissing(appointmentSettingsCollection, appointmentSettings);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AppointmentSettings to an array that doesn't contain it", () => {
          const appointmentSettings: IAppointmentSettings = { id: 123 };
          const appointmentSettingsCollection: IAppointmentSettings[] = [{ id: 456 }];
          expectedResult = service.addAppointmentSettingsToCollectionIfMissing(appointmentSettingsCollection, appointmentSettings);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentSettings);
        });

        it('should add only unique AppointmentSettings to an array', () => {
          const appointmentSettingsArray: IAppointmentSettings[] = [{ id: 123 }, { id: 456 }, { id: 40524 }];
          const appointmentSettingsCollection: IAppointmentSettings[] = [{ id: 123 }];
          expectedResult = service.addAppointmentSettingsToCollectionIfMissing(appointmentSettingsCollection, ...appointmentSettingsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appointmentSettings: IAppointmentSettings = { id: 123 };
          const appointmentSettings2: IAppointmentSettings = { id: 456 };
          expectedResult = service.addAppointmentSettingsToCollectionIfMissing([], appointmentSettings, appointmentSettings2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentSettings);
          expect(expectedResult).toContain(appointmentSettings2);
        });

        it('should accept null and undefined values', () => {
          const appointmentSettings: IAppointmentSettings = { id: 123 };
          expectedResult = service.addAppointmentSettingsToCollectionIfMissing([], null, appointmentSettings, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentSettings);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
