import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHoliday } from '../holiday.model';
import { HolidayService } from '../service/holiday.service';

@Component({
  templateUrl: './holiday-delete-dialog.component.html',
})
export class HolidayDeleteDialogComponent {
  holiday?: IHoliday;

  constructor(protected holidayService: HolidayService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.holidayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
