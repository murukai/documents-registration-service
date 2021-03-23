import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppointmentSlotComponent } from '../list/appointment-slot.component';
import { AppointmentSlotDetailComponent } from '../detail/appointment-slot-detail.component';
import { AppointmentSlotUpdateComponent } from '../update/appointment-slot-update.component';
import { AppointmentSlotRoutingResolveService } from './appointment-slot-routing-resolve.service';

const appointmentSlotRoute: Routes = [
  {
    path: '',
    component: AppointmentSlotComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppointmentSlotDetailComponent,
    resolve: {
      appointmentSlot: AppointmentSlotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppointmentSlotUpdateComponent,
    resolve: {
      appointmentSlot: AppointmentSlotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppointmentSlotUpdateComponent,
    resolve: {
      appointmentSlot: AppointmentSlotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appointmentSlotRoute)],
  exports: [RouterModule],
})
export class AppointmentSlotRoutingModule {}
