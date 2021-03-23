import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AppointmentSlotComponent } from './list/appointment-slot.component';
import { AppointmentSlotDetailComponent } from './detail/appointment-slot-detail.component';
import { AppointmentSlotUpdateComponent } from './update/appointment-slot-update.component';
import { AppointmentSlotDeleteDialogComponent } from './delete/appointment-slot-delete-dialog.component';
import { AppointmentSlotRoutingModule } from './route/appointment-slot-routing.module';

@NgModule({
  imports: [SharedModule, AppointmentSlotRoutingModule],
  declarations: [
    AppointmentSlotComponent,
    AppointmentSlotDetailComponent,
    AppointmentSlotUpdateComponent,
    AppointmentSlotDeleteDialogComponent,
  ],
  entryComponents: [AppointmentSlotDeleteDialogComponent],
})
export class AppointmentSlotModule {}
