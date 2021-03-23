import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointmentSettings } from '../appointment-settings.model';
import { AppointmentSettingsService } from '../service/appointment-settings.service';

@Component({
  templateUrl: './appointment-settings-delete-dialog.component.html',
})
export class AppointmentSettingsDeleteDialogComponent {
  appointmentSettings?: IAppointmentSettings;

  constructor(protected appointmentSettingsService: AppointmentSettingsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appointmentSettingsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
