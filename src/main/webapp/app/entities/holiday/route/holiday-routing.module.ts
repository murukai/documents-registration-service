import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HolidayComponent } from '../list/holiday.component';
import { HolidayDetailComponent } from '../detail/holiday-detail.component';
import { HolidayUpdateComponent } from '../update/holiday-update.component';
import { HolidayRoutingResolveService } from './holiday-routing-resolve.service';

const holidayRoute: Routes = [
  {
    path: '',
    component: HolidayComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HolidayDetailComponent,
    resolve: {
      holiday: HolidayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HolidayUpdateComponent,
    resolve: {
      holiday: HolidayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HolidayUpdateComponent,
    resolve: {
      holiday: HolidayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(holidayRoute)],
  exports: [RouterModule],
})
export class HolidayRoutingModule {}
