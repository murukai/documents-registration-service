import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAppointment, Appointment } from '../appointment.model';
import { AppointmentService } from '../service/appointment.service';

@Component({
  selector: 'jhi-appointment-update',
  templateUrl: './appointment-update.component.html',
})
export class AppointmentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dateOfAppointment: [null, [Validators.required]],
    available: [null, [Validators.required]],
    appointmentType: [],
    maxAppointments: [null, [Validators.required, Validators.min(0), Validators.max(1000)]],
  });

  constructor(protected appointmentService: AppointmentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointment }) => {
      if (appointment.id === undefined) {
        const today = dayjs().startOf('day');
        appointment.dateOfAppointment = today;
      }

      this.updateForm(appointment);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointment = this.createFromForm();
    if (appointment.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentService.update(appointment));
    } else {
      this.subscribeToSaveResponse(this.appointmentService.create(appointment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointment>>): void {
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

  protected updateForm(appointment: IAppointment): void {
    this.editForm.patchValue({
      id: appointment.id,
      dateOfAppointment: appointment.dateOfAppointment ? appointment.dateOfAppointment.format(DATE_TIME_FORMAT) : null,
      available: appointment.available,
      appointmentType: appointment.appointmentType,
      maxAppointments: appointment.maxAppointments,
    });
  }

  protected createFromForm(): IAppointment {
    return {
      ...new Appointment(),
      id: this.editForm.get(['id'])!.value,
      dateOfAppointment: this.editForm.get(['dateOfAppointment'])!.value
        ? dayjs(this.editForm.get(['dateOfAppointment'])!.value, DATE_TIME_FORMAT)
        : undefined,
      available: this.editForm.get(['available'])!.value,
      appointmentType: this.editForm.get(['appointmentType'])!.value,
      maxAppointments: this.editForm.get(['maxAppointments'])!.value,
    };
  }
}
