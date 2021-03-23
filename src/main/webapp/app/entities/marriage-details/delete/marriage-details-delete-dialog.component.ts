import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMarriageDetails } from '../marriage-details.model';
import { MarriageDetailsService } from '../service/marriage-details.service';

@Component({
  templateUrl: './marriage-details-delete-dialog.component.html',
})
export class MarriageDetailsDeleteDialogComponent {
  marriageDetails?: IMarriageDetails;

  constructor(protected marriageDetailsService: MarriageDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.marriageDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
