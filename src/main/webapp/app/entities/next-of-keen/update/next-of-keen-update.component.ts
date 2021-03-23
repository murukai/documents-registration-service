import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INextOfKeen, NextOfKeen } from '../next-of-keen.model';
import { NextOfKeenService } from '../service/next-of-keen.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

@Component({
  selector: 'jhi-next-of-keen-update',
  templateUrl: './next-of-keen-update.component.html',
})
export class NextOfKeenUpdateComponent implements OnInit {
  isSaving = false;

  applicantsCollection: IApplicant[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required]],
    address: [null, [Validators.required]],
    cellphone: [null, [Validators.required]],
    applicant: [null, Validators.required],
  });

  constructor(
    protected nextOfKeenService: NextOfKeenService,
    protected applicantService: ApplicantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nextOfKeen }) => {
      this.updateForm(nextOfKeen);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nextOfKeen = this.createFromForm();
    if (nextOfKeen.id !== undefined) {
      this.subscribeToSaveResponse(this.nextOfKeenService.update(nextOfKeen));
    } else {
      this.subscribeToSaveResponse(this.nextOfKeenService.create(nextOfKeen));
    }
  }

  trackApplicantById(index: number, item: IApplicant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INextOfKeen>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(nextOfKeen: INextOfKeen): void {
    this.editForm.patchValue({
      id: nextOfKeen.id,
      fullName: nextOfKeen.fullName,
      address: nextOfKeen.address,
      cellphone: nextOfKeen.cellphone,
      applicant: nextOfKeen.applicant,
    });

    this.applicantsCollection = this.applicantService.addApplicantToCollectionIfMissing(this.applicantsCollection, nextOfKeen.applicant);
  }

  protected loadRelationshipsOptions(): void {
    this.applicantService
      .query({ filter: 'nextofkeen-is-null' })
      .pipe(map((res: HttpResponse<IApplicant[]>) => res.body ?? []))
      .pipe(
        map((applicants: IApplicant[]) =>
          this.applicantService.addApplicantToCollectionIfMissing(applicants, this.editForm.get('applicant')!.value)
        )
      )
      .subscribe((applicants: IApplicant[]) => (this.applicantsCollection = applicants));
  }

  protected createFromForm(): INextOfKeen {
    return {
      ...new NextOfKeen(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      address: this.editForm.get(['address'])!.value,
      cellphone: this.editForm.get(['cellphone'])!.value,
      applicant: this.editForm.get(['applicant'])!.value,
    };
  }
}
