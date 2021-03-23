jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAppointmentSettings, AppointmentSettings } from '../appointment-settings.model';
import { AppointmentSettingsService } from '../service/appointment-settings.service';

import { AppointmentSettingsRoutingResolveService } from './appointment-settings-routing-resolve.service';

describe('Service Tests', () => {
  describe('AppointmentSettings routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AppointmentSettingsRoutingResolveService;
    let service: AppointmentSettingsService;
    let resultAppointmentSettings: IAppointmentSettings | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AppointmentSettingsRoutingResolveService);
      service = TestBed.inject(AppointmentSettingsService);
      resultAppointmentSettings = undefined;
    });

    describe('resolve', () => {
      it('should return IAppointmentSettings returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentSettings = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAppointmentSettings).toEqual({ id: 123 });
      });

      it('should return new IAppointmentSettings if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentSettings = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAppointmentSettings).toEqual(new AppointmentSettings());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAppointmentSettings = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAppointmentSettings).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
