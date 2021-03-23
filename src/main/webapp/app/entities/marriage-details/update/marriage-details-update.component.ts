import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMarriageDetails, MarriageDetails } from '../marriage-details.model';
import { MarriageDetailsService } from '../service/marriage-details.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

@Component({
  selector: 'jhi-marriage-details-update',
  templateUrl: './marriage-details-update.component.html',
})
export class MarriageDetailsUpdateComponent implements OnInit {
  isSaving = false;

  applicantsCollection: IApplicant[] = [];
  countryOfMarriagesCollection: ICountry[] = [];
  spouseCountryOfBirthsCollection: ICountry[] = [];

  editForm = this.fb.group({
    id: [],
    dateOfMarriage: [null, [Validators.required]],
    spouseFullName: [null, [Validators.required]],
    placeOfMarriage: [null, [Validators.required]],
    spousePlaceOfBirth: [null, [Validators.required]],
    marriageNumber: [null, [Validators.required]],
    marriedBefore: [null, [Validators.required]],
    applicant: [null, Validators.required],
    countryOfMarriage: [null, Validators.required],
    spouseCountryOfBirth: [null, Validators.required],
  });

  constructor(
    protected marriageDetailsService: MarriageDetailsService,
    protected applicantService: ApplicantService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marriageDetails }) => {
      this.updateForm(marriageDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const marriageDetails = this.createFromForm();
    if (marriageDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.marriageDetailsService.update(marriageDetails));
    } else {
      this.subscribeToSaveResponse(this.marriageDetailsService.create(marriageDetails));
    }
  }

  trackApplicantById(index: number, item: IApplicant): number {
    return item.id!;
  }

  trackCountryById(index: number, item: ICountry): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarriageDetails>>): void {
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

  protected updateForm(marriageDetails: IMarriageDetails): void {
    this.editForm.patchValue({
      id: marriageDetails.id,
      dateOfMarriage: marriageDetails.dateOfMarriage,
      spouseFullName: marriageDetails.spouseFullName,
      placeOfMarriage: marriageDetails.placeOfMarriage,
      spousePlaceOfBirth: marriageDetails.spousePlaceOfBirth,
      marriageNumber: marriageDetails.marriageNumber,
      marriedBefore: marriageDetails.marriedBefore,
      applicant: marriageDetails.applicant,
      countryOfMarriage: marriageDetails.countryOfMarriage,
      spouseCountryOfBirth: marriageDetails.spouseCountryOfBirth,
    });

    this.applicantsCollection = this.applicantService.addApplicantToCollectionIfMissing(
      this.applicantsCollection,
      marriageDetails.applicant
    );
    this.countryOfMarriagesCollection = this.countryService.addCountryToCollectionIfMissing(
      this.countryOfMarriagesCollection,
      marriageDetails.countryOfMarriage
    );
    this.spouseCountryOfBirthsCollection = this.countryService.addCountryToCollectionIfMissing(
      this.spouseCountryOfBirthsCollection,
      marriageDetails.spouseCountryOfBirth
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicantService
      .query({ filter: 'marriagedetails-is-null' })
      .pipe(map((res: HttpResponse<IApplicant[]>) => res.body ?? []))
      .pipe(
        map((applicants: IApplicant[]) =>
          this.applicantService.addApplicantToCollectionIfMissing(applicants, this.editForm.get('applicant')!.value)
        )
      )
      .subscribe((applicants: IApplicant[]) => (this.applicantsCollection = applicants));

    this.countryService
      .query({ filter: 'marriagedetails-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('countryOfMarriage')!.value)
        )
      )
      .subscribe((countries: ICountry[]) => (this.countryOfMarriagesCollection = countries));

    this.countryService
      .query({ filter: 'marriagedetails-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('spouseCountryOfBirth')!.value)
        )
      )
      .subscribe((countries: ICountry[]) => (this.spouseCountryOfBirthsCollection = countries));
  }

  protected createFromForm(): IMarriageDetails {
    return {
      ...new MarriageDetails(),
      id: this.editForm.get(['id'])!.value,
      dateOfMarriage: this.editForm.get(['dateOfMarriage'])!.value,
      spouseFullName: this.editForm.get(['spouseFullName'])!.value,
      placeOfMarriage: this.editForm.get(['placeOfMarriage'])!.value,
      spousePlaceOfBirth: this.editForm.get(['spousePlaceOfBirth'])!.value,
      marriageNumber: this.editForm.get(['marriageNumber'])!.value,
      marriedBefore: this.editForm.get(['marriedBefore'])!.value,
      applicant: this.editForm.get(['applicant'])!.value,
      countryOfMarriage: this.editForm.get(['countryOfMarriage'])!.value,
      spouseCountryOfBirth: this.editForm.get(['spouseCountryOfBirth'])!.value,
    };
  }
}
