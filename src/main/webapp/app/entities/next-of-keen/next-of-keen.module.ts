import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NextOfKeenComponent } from './list/next-of-keen.component';
import { NextOfKeenDetailComponent } from './detail/next-of-keen-detail.component';
import { NextOfKeenUpdateComponent } from './update/next-of-keen-update.component';
import { NextOfKeenDeleteDialogComponent } from './delete/next-of-keen-delete-dialog.component';
import { NextOfKeenRoutingModule } from './route/next-of-keen-routing.module';

@NgModule({
  imports: [SharedModule, NextOfKeenRoutingModule],
  declarations: [NextOfKeenComponent, NextOfKeenDetailComponent, NextOfKeenUpdateComponent, NextOfKeenDeleteDialogComponent],
  entryComponents: [NextOfKeenDeleteDialogComponent],
})
export class NextOfKeenModule {}
