import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAppointmentSettings, AppointmentSettings } from '../appointment-settings.model';
import { AppointmentSettingsService } from '../service/appointment-settings.service';

@Component({
  selector: 'jhi-appointment-settings-update',
  templateUrl: './appointment-settings-update.component.html',
})
export class AppointmentSettingsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    maxOrdinaryAppointments: [null, [Validators.required, Validators.min(0), Validators.max(1000)]],
    maxEmergencyAppointments: [null, [Validators.required, Validators.min(0), Validators.max(1000)]],
    startingWorkTime: [null, [Validators.required, Validators.min(0), Validators.max(23)]],
    endingWorkTime: [null, [Validators.required, Validators.min(0), Validators.max(23)]],
  });

  constructor(
    protected appointmentSettingsService: AppointmentSettingsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentSettings }) => {
      this.updateForm(appointmentSettings);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointmentSettings = this.createFromForm();
    if (appointmentSettings.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentSettingsService.update(appointmentSettings));
    } else {
      this.subscribeToSaveResponse(this.appointmentSettingsService.create(appointmentSettings));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointmentSettings>>): void {
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

  protected updateForm(appointmentSettings: IAppointmentSettings): void {
    this.editForm.patchValue({
      id: appointmentSettings.id,
      maxOrdinaryAppointments: appointmentSettings.maxOrdinaryAppointments,
      maxEmergencyAppointments: appointmentSettings.maxEmergencyAppointments,
      startingWorkTime: appointmentSettings.startingWorkTime,
      endingWorkTime: appointmentSettings.endingWorkTime,
    });
  }

  protected createFromForm(): IAppointmentSettings {
    return {
      ...new AppointmentSettings(),
      id: this.editForm.get(['id'])!.value,
      maxOrdinaryAppointments: this.editForm.get(['maxOrdinaryAppointments'])!.value,
      maxEmergencyAppointments: this.editForm.get(['maxEmergencyAppointments'])!.value,
      startingWorkTime: this.editForm.get(['startingWorkTime'])!.value,
      endingWorkTime: this.editForm.get(['endingWorkTime'])!.value,
    };
  }
}
