import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApplicant } from '../applicant.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-applicant-detail',
  templateUrl: './applicant-detail.component.html',
})
export class ApplicantDetailComponent implements OnInit {
  applicant: IApplicant | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicant }) => {
      this.applicant = applicant;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
