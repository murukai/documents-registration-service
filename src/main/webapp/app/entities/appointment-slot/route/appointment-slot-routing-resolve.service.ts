import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppointmentSlot, AppointmentSlot } from '../appointment-slot.model';
import { AppointmentSlotService } from '../service/appointment-slot.service';

@Injectable({ providedIn: 'root' })
export class AppointmentSlotRoutingResolveService implements Resolve<IAppointmentSlot> {
  constructor(protected service: AppointmentSlotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppointmentSlot> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appointmentSlot: HttpResponse<AppointmentSlot>) => {
          if (appointmentSlot.body) {
            return of(appointmentSlot.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppointmentSlot());
  }
}
