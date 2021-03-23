jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMarriageDetails, MarriageDetails } from '../marriage-details.model';
import { MarriageDetailsService } from '../service/marriage-details.service';

import { MarriageDetailsRoutingResolveService } from './marriage-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('MarriageDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MarriageDetailsRoutingResolveService;
    let service: MarriageDetailsService;
    let resultMarriageDetails: IMarriageDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MarriageDetailsRoutingResolveService);
      service = TestBed.inject(MarriageDetailsService);
      resultMarriageDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IMarriageDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMarriageDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMarriageDetails).toEqual({ id: 123 });
      });

      it('should return new IMarriageDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMarriageDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMarriageDetails).toEqual(new MarriageDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMarriageDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMarriageDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
