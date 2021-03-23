import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INextOfKeen } from '../next-of-keen.model';
import { NextOfKeenService } from '../service/next-of-keen.service';

@Component({
  templateUrl: './next-of-keen-delete-dialog.component.html',
})
export class NextOfKeenDeleteDialogComponent {
  nextOfKeen?: INextOfKeen;

  constructor(protected nextOfKeenService: NextOfKeenService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nextOfKeenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
