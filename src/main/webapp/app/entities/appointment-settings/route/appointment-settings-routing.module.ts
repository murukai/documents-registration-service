import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppointmentSettingsComponent } from '../list/appointment-settings.component';
import { AppointmentSettingsDetailComponent } from '../detail/appointment-settings-detail.component';
import { AppointmentSettingsUpdateComponent } from '../update/appointment-settings-update.component';
import { AppointmentSettingsRoutingResolveService } from './appointment-settings-routing-resolve.service';

const appointmentSettingsRoute: Routes = [
  {
    path: '',
    component: AppointmentSettingsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppointmentSettingsDetailComponent,
    resolve: {
      appointmentSettings: AppointmentSettingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppointmentSettingsUpdateComponent,
    resolve: {
      appointmentSettings: AppointmentSettingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppointmentSettingsUpdateComponent,
    resolve: {
      appointmentSettings: AppointmentSettingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appointmentSettingsRoute)],
  exports: [RouterModule],
})
export class AppointmentSettingsRoutingModule {}
