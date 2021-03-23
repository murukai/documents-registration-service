import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMarriageDetails, MarriageDetails } from '../marriage-details.model';
import { MarriageDetailsService } from '../service/marriage-details.service';

@Injectable({ providedIn: 'root' })
export class MarriageDetailsRoutingResolveService implements Resolve<IMarriageDetails> {
  constructor(protected service: MarriageDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMarriageDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((marriageDetails: HttpResponse<MarriageDetails>) => {
          if (marriageDetails.body) {
            return of(marriageDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MarriageDetails());
  }
}
