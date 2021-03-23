jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INextOfKeen, NextOfKeen } from '../next-of-keen.model';
import { NextOfKeenService } from '../service/next-of-keen.service';

import { NextOfKeenRoutingResolveService } from './next-of-keen-routing-resolve.service';

describe('Service Tests', () => {
  describe('NextOfKeen routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NextOfKeenRoutingResolveService;
    let service: NextOfKeenService;
    let resultNextOfKeen: INextOfKeen | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NextOfKeenRoutingResolveService);
      service = TestBed.inject(NextOfKeenService);
      resultNextOfKeen = undefined;
    });

    describe('resolve', () => {
      it('should return INextOfKeen returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNextOfKeen = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNextOfKeen).toEqual({ id: 123 });
      });

      it('should return new INextOfKeen if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNextOfKeen = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNextOfKeen).toEqual(new NextOfKeen());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNextOfKeen = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNextOfKeen).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
