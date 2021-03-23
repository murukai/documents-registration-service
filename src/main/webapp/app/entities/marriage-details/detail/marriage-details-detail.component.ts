import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMarriageDetails } from '../marriage-details.model';

@Component({
  selector: 'jhi-marriage-details-detail',
  templateUrl: './marriage-details-detail.component.html',
})
export class MarriageDetailsDetailComponent implements OnInit {
  marriageDetails: IMarriageDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marriageDetails }) => {
      this.marriageDetails = marriageDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
