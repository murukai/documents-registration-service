import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApplicant, Applicant } from '../applicant.model';
import { ApplicantService } from '../service/applicant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { AppointmentSlotService } from 'app/entities/appointment-slot/service/appointment-slot.service';

@Component({
  selector: 'jhi-applicant-update',
  templateUrl: './applicant-update.component.html',
})
export class ApplicantUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  appointmentSlotsSharedCollection: IAppointmentSlot[] = [];

  editForm = this.fb.group({
    id: [],
    firstNames: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    initials: [null, [Validators.required]],
    gender: [],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    maritalStatus: [],
    dateOfBirth: [null, [Validators.required]],
    idNumber: [null, [Validators.required]],
    birthEntryNumber: [null, [Validators.required]],
    eyeColor: [null, [Validators.required]],
    hairColor: [null, [Validators.required]],
    visibleMarks: [null, [Validators.required]],
    profession: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    user: [null, Validators.required],
    appointmentSlot: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected applicantService: ApplicantService,
    protected userService: UserService,
    protected appointmentSlotService: AppointmentSlotService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicant }) => {
      this.updateForm(applicant);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('documentsregistrationserviceApp.error', { message: err.message })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicant = this.createFromForm();
    if (applicant.id !== undefined) {
      this.subscribeToSaveResponse(this.applicantService.update(applicant));
    } else {
      this.subscribeToSaveResponse(this.applicantService.create(applicant));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackAppointmentSlotById(index: number, item: IAppointmentSlot): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicant>>): void {
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

  protected updateForm(applicant: IApplicant): void {
    this.editForm.patchValue({
      id: applicant.id,
      firstNames: applicant.firstNames,
      lastName: applicant.lastName,
      initials: applicant.initials,
      gender: applicant.gender,
      email: applicant.email,
      maritalStatus: applicant.maritalStatus,
      dateOfBirth: applicant.dateOfBirth,
      idNumber: applicant.idNumber,
      birthEntryNumber: applicant.birthEntryNumber,
      eyeColor: applicant.eyeColor,
      hairColor: applicant.hairColor,
      visibleMarks: applicant.visibleMarks,
      profession: applicant.profession,
      image: applicant.image,
      imageContentType: applicant.imageContentType,
      user: applicant.user,
      appointmentSlot: applicant.appointmentSlot,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, applicant.user);
    this.appointmentSlotsSharedCollection = this.appointmentSlotService.addAppointmentSlotToCollectionIfMissing(
      this.appointmentSlotsSharedCollection,
      applicant.appointmentSlot
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.appointmentSlotService
      .query()
      .pipe(map((res: HttpResponse<IAppointmentSlot[]>) => res.body ?? []))
      .pipe(
        map((appointmentSlots: IAppointmentSlot[]) =>
          this.appointmentSlotService.addAppointmentSlotToCollectionIfMissing(appointmentSlots, this.editForm.get('appointmentSlot')!.value)
        )
      )
      .subscribe((appointmentSlots: IAppointmentSlot[]) => (this.appointmentSlotsSharedCollection = appointmentSlots));
  }

  protected createFromForm(): IApplicant {
    return {
      ...new Applicant(),
      id: this.editForm.get(['id'])!.value,
      firstNames: this.editForm.get(['firstNames'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      initials: this.editForm.get(['initials'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      email: this.editForm.get(['email'])!.value,
      maritalStatus: this.editForm.get(['maritalStatus'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      idNumber: this.editForm.get(['idNumber'])!.value,
      birthEntryNumber: this.editForm.get(['birthEntryNumber'])!.value,
      eyeColor: this.editForm.get(['eyeColor'])!.value,
      hairColor: this.editForm.get(['hairColor'])!.value,
      visibleMarks: this.editForm.get(['visibleMarks'])!.value,
      profession: this.editForm.get(['profession'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      user: this.editForm.get(['user'])!.value,
      appointmentSlot: this.editForm.get(['appointmentSlot'])!.value,
    };
  }
}
