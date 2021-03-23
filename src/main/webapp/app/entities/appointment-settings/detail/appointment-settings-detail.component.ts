import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppointmentSettings } from '../appointment-settings.model';

@Component({
  selector: 'jhi-appointment-settings-detail',
  templateUrl: './appointment-settings-detail.component.html',
})
export class AppointmentSettingsDetailComponent implements OnInit {
  appointmentSettings: IAppointmentSettings | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentSettings }) => {
      this.appointmentSettings = appointmentSettings;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
