import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppointmentSlot } from '../appointment-slot.model';

@Component({
  selector: 'jhi-appointment-slot-detail',
  templateUrl: './appointment-slot-detail.component.html',
})
export class AppointmentSlotDetailComponent implements OnInit {
  appointmentSlot: IAppointmentSlot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentSlot }) => {
      this.appointmentSlot = appointmentSlot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
