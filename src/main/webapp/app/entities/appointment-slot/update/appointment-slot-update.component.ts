import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAppointmentSlot, AppointmentSlot } from '../appointment-slot.model';
import { AppointmentSlotService } from '../service/appointment-slot.service';
import { IAppointment } from 'app/entities/appointment/appointment.model';
import { AppointmentService } from 'app/entities/appointment/service/appointment.service';

@Component({
  selector: 'jhi-appointment-slot-update',
  templateUrl: './appointment-slot-update.component.html',
})
export class AppointmentSlotUpdateComponent implements OnInit {
  isSaving = false;

  appointmentsSharedCollection: IAppointment[] = [];

  editForm = this.fb.group({
    id: [],
    timeOfAppointment: [null, [Validators.required]],
    available: [null, [Validators.required]],
    maxAppointments: [null, [Validators.required, Validators.min(1), Validators.max(100)]],
    appointment: [],
  });

  constructor(
    protected appointmentSlotService: AppointmentSlotService,
    protected appointmentService: AppointmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentSlot }) => {
      if (appointmentSlot.id === undefined) {
        const today = dayjs().startOf('day');
        appointmentSlot.timeOfAppointment = today;
      }

      this.updateForm(appointmentSlot);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointmentSlot = this.createFromForm();
    if (appointmentSlot.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentSlotService.update(appointmentSlot));
    } else {
      this.subscribeToSaveResponse(this.appointmentSlotService.create(appointmentSlot));
    }
  }

  trackAppointmentById(index: number, item: IAppointment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointmentSlot>>): void {
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

  protected updateForm(appointmentSlot: IAppointmentSlot): void {
    this.editForm.patchValue({
      id: appointmentSlot.id,
      timeOfAppointment: appointmentSlot.timeOfAppointment ? appointmentSlot.timeOfAppointment.format(DATE_TIME_FORMAT) : null,
      available: appointmentSlot.available,
      maxAppointments: appointmentSlot.maxAppointments,
      appointment: appointmentSlot.appointment,
    });

    this.appointmentsSharedCollection = this.appointmentService.addAppointmentToCollectionIfMissing(
      this.appointmentsSharedCollection,
      appointmentSlot.appointment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appointmentService
      .query()
      .pipe(map((res: HttpResponse<IAppointment[]>) => res.body ?? []))
      .pipe(
        map((appointments: IAppointment[]) =>
          this.appointmentService.addAppointmentToCollectionIfMissing(appointments, this.editForm.get('appointment')!.value)
        )
      )
      .subscribe((appointments: IAppointment[]) => (this.appointmentsSharedCollection = appointments));
  }

  protected createFromForm(): IAppointmentSlot {
    return {
      ...new AppointmentSlot(),
      id: this.editForm.get(['id'])!.value,
      timeOfAppointment: this.editForm.get(['timeOfAppointment'])!.value
        ? dayjs(this.editForm.get(['timeOfAppointment'])!.value, DATE_TIME_FORMAT)
        : undefined,
      available: this.editForm.get(['available'])!.value,
      maxAppointments: this.editForm.get(['maxAppointments'])!.value,
      appointment: this.editForm.get(['appointment'])!.value,
    };
  }
}
