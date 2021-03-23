import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAddress, Address } from '../address.model';
import { AddressService } from '../service/address.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

@Component({
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html',
})
export class AddressUpdateComponent implements OnInit {
  isSaving = false;

  countriesCollection: ICountry[] = [];
  countryOfBirthsCollection: ICountry[] = [];
  countryOfResidencesCollection: ICountry[] = [];
  applicantsSharedCollection: IApplicant[] = [];

  editForm = this.fb.group({
    id: [],
    streetAddress: [null, [Validators.required]],
    postalCode: [null, [Validators.required]],
    city: [null, [Validators.required]],
    stateProvince: [],
    placeOfBirth: [null, [Validators.required]],
    country: [null, Validators.required],
    countryOfBirth: [null, Validators.required],
    countryOfResidence: [null, Validators.required],
    applicant: [],
  });

  constructor(
    protected addressService: AddressService,
    protected countryService: CountryService,
    protected applicantService: ApplicantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.updateForm(address);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const address = this.createFromForm();
    if (address.id !== undefined) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  trackCountryById(index: number, item: ICountry): number {
    return item.id!;
  }

  trackApplicantById(index: number, item: IApplicant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>): void {
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

  protected updateForm(address: IAddress): void {
    this.editForm.patchValue({
      id: address.id,
      streetAddress: address.streetAddress,
      postalCode: address.postalCode,
      city: address.city,
      stateProvince: address.stateProvince,
      placeOfBirth: address.placeOfBirth,
      country: address.country,
      countryOfBirth: address.countryOfBirth,
      countryOfResidence: address.countryOfResidence,
      applicant: address.applicant,
    });

    this.countriesCollection = this.countryService.addCountryToCollectionIfMissing(this.countriesCollection, address.country);
    this.countryOfBirthsCollection = this.countryService.addCountryToCollectionIfMissing(
      this.countryOfBirthsCollection,
      address.countryOfBirth
    );
    this.countryOfResidencesCollection = this.countryService.addCountryToCollectionIfMissing(
      this.countryOfResidencesCollection,
      address.countryOfResidence
    );
    this.applicantsSharedCollection = this.applicantService.addApplicantToCollectionIfMissing(
      this.applicantsSharedCollection,
      address.applicant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query({ filter: 'address-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('country')!.value))
      )
      .subscribe((countries: ICountry[]) => (this.countriesCollection = countries));

    this.countryService
      .query({ filter: 'address-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('countryOfBirth')!.value)
        )
      )
      .subscribe((countries: ICountry[]) => (this.countryOfBirthsCollection = countries));

    this.countryService
      .query({ filter: 'address-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('countryOfResidence')!.value)
        )
      )
      .subscribe((countries: ICountry[]) => (this.countryOfResidencesCollection = countries));

    this.applicantService
      .query()
      .pipe(map((res: HttpResponse<IApplicant[]>) => res.body ?? []))
      .pipe(
        map((applicants: IApplicant[]) =>
          this.applicantService.addApplicantToCollectionIfMissing(applicants, this.editForm.get('applicant')!.value)
        )
      )
      .subscribe((applicants: IApplicant[]) => (this.applicantsSharedCollection = applicants));
  }

  protected createFromForm(): IAddress {
    return {
      ...new Address(),
      id: this.editForm.get(['id'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      city: this.editForm.get(['city'])!.value,
      stateProvince: this.editForm.get(['stateProvince'])!.value,
      placeOfBirth: this.editForm.get(['placeOfBirth'])!.value,
      country: this.editForm.get(['country'])!.value,
      countryOfBirth: this.editForm.get(['countryOfBirth'])!.value,
      countryOfResidence: this.editForm.get(['countryOfResidence'])!.value,
      applicant: this.editForm.get(['applicant'])!.value,
    };
  }
}
