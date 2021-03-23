import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { HolidayComponent } from './list/holiday.component';
import { HolidayDetailComponent } from './detail/holiday-detail.component';
import { HolidayUpdateComponent } from './update/holiday-update.component';
import { HolidayDeleteDialogComponent } from './delete/holiday-delete-dialog.component';
import { HolidayRoutingModule } from './route/holiday-routing.module';

@NgModule({
  imports: [SharedModule, HolidayRoutingModule],
  declarations: [HolidayComponent, HolidayDetailComponent, HolidayUpdateComponent, HolidayDeleteDialogComponent],
  entryComponents: [HolidayDeleteDialogComponent],
})
export class HolidayModule {}
