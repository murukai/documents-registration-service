import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INextOfKeen, NextOfKeen } from '../next-of-keen.model';
import { NextOfKeenService } from '../service/next-of-keen.service';

@Injectable({ providedIn: 'root' })
export class NextOfKeenRoutingResolveService implements Resolve<INextOfKeen> {
  constructor(protected service: NextOfKeenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INextOfKeen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nextOfKeen: HttpResponse<NextOfKeen>) => {
          if (nextOfKeen.body) {
            return of(nextOfKeen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NextOfKeen());
  }
}
