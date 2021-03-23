import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AppointmentSettingsComponent } from './list/appointment-settings.component';
import { AppointmentSettingsDetailComponent } from './detail/appointment-settings-detail.component';
import { AppointmentSettingsUpdateComponent } from './update/appointment-settings-update.component';
import { AppointmentSettingsDeleteDialogComponent } from './delete/appointment-settings-delete-dialog.component';
import { AppointmentSettingsRoutingModule } from './route/appointment-settings-routing.module';

@NgModule({
  imports: [SharedModule, AppointmentSettingsRoutingModule],
  declarations: [
    AppointmentSettingsComponent,
    AppointmentSettingsDetailComponent,
    AppointmentSettingsUpdateComponent,
    AppointmentSettingsDeleteDialogComponent,
  ],
  entryComponents: [AppointmentSettingsDeleteDialogComponent],
})
export class AppointmentSettingsModule {}
