import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MarriageDetailsComponent } from './list/marriage-details.component';
import { MarriageDetailsDetailComponent } from './detail/marriage-details-detail.component';
import { MarriageDetailsUpdateComponent } from './update/marriage-details-update.component';
import { MarriageDetailsDeleteDialogComponent } from './delete/marriage-details-delete-dialog.component';
import { MarriageDetailsRoutingModule } from './route/marriage-details-routing.module';

@NgModule({
  imports: [SharedModule, MarriageDetailsRoutingModule],
  declarations: [
    MarriageDetailsComponent,
    MarriageDetailsDetailComponent,
    MarriageDetailsUpdateComponent,
    MarriageDetailsDeleteDialogComponent,
  ],
  entryComponents: [MarriageDetailsDeleteDialogComponent],
})
export class MarriageDetailsModule {}
