import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppointmentSettings, AppointmentSettings } from '../appointment-settings.model';
import { AppointmentSettingsService } from '../service/appointment-settings.service';

@Injectable({ providedIn: 'root' })
export class AppointmentSettingsRoutingResolveService implements Resolve<IAppointmentSettings> {
  constructor(protected service: AppointmentSettingsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppointmentSettings> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appointmentSettings: HttpResponse<AppointmentSettings>) => {
          if (appointmentSettings.body) {
            return of(appointmentSettings.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppointmentSettings());
  }
}
