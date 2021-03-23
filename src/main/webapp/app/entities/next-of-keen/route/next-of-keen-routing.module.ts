import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NextOfKeenComponent } from '../list/next-of-keen.component';
import { NextOfKeenDetailComponent } from '../detail/next-of-keen-detail.component';
import { NextOfKeenUpdateComponent } from '../update/next-of-keen-update.component';
import { NextOfKeenRoutingResolveService } from './next-of-keen-routing-resolve.service';

const nextOfKeenRoute: Routes = [
  {
    path: '',
    component: NextOfKeenComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NextOfKeenDetailComponent,
    resolve: {
      nextOfKeen: NextOfKeenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NextOfKeenUpdateComponent,
    resolve: {
      nextOfKeen: NextOfKeenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NextOfKeenUpdateComponent,
    resolve: {
      nextOfKeen: NextOfKeenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nextOfKeenRoute)],
  exports: [RouterModule],
})
export class NextOfKeenRoutingModule {}
