import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointmentSlot } from '../appointment-slot.model';
import { AppointmentSlotService } from '../service/appointment-slot.service';

@Component({
  templateUrl: './appointment-slot-delete-dialog.component.html',
})
export class AppointmentSlotDeleteDialogComponent {
  appointmentSlot?: IAppointmentSlot;

  constructor(protected appointmentSlotService: AppointmentSlotService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appointmentSlotService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
