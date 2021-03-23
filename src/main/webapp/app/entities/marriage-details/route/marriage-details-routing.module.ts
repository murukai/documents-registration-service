import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MarriageDetailsComponent } from '../list/marriage-details.component';
import { MarriageDetailsDetailComponent } from '../detail/marriage-details-detail.component';
import { MarriageDetailsUpdateComponent } from '../update/marriage-details-update.component';
import { MarriageDetailsRoutingResolveService } from './marriage-details-routing-resolve.service';

const marriageDetailsRoute: Routes = [
  {
    path: '',
    component: MarriageDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MarriageDetailsDetailComponent,
    resolve: {
      marriageDetails: MarriageDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MarriageDetailsUpdateComponent,
    resolve: {
      marriageDetails: MarriageDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MarriageDetailsUpdateComponent,
    resolve: {
      marriageDetails: MarriageDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(marriageDetailsRoute)],
  exports: [RouterModule],
})
export class MarriageDetailsRoutingModule {}
