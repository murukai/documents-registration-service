import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'address',
        data: { pageTitle: 'Addresses' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'applicant',
        data: { pageTitle: 'Applicants' },
        loadChildren: () => import('./applicant/applicant.module').then(m => m.ApplicantModule),
      },
      {
        path: 'appointment',
        data: { pageTitle: 'Appointments' },
        loadChildren: () => import('./appointment/appointment.module').then(m => m.AppointmentModule),
      },
      {
        path: 'appointment-settings',
        data: { pageTitle: 'AppointmentSettings' },
        loadChildren: () => import('./appointment-settings/appointment-settings.module').then(m => m.AppointmentSettingsModule),
      },
      {
        path: 'appointment-slot',
        data: { pageTitle: 'AppointmentSlots' },
        loadChildren: () => import('./appointment-slot/appointment-slot.module').then(m => m.AppointmentSlotModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'Countries' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'holiday',
        data: { pageTitle: 'Holidays' },
        loadChildren: () => import('./holiday/holiday.module').then(m => m.HolidayModule),
      },
      {
        path: 'marriage-details',
        data: { pageTitle: 'MarriageDetails' },
        loadChildren: () => import('./marriage-details/marriage-details.module').then(m => m.MarriageDetailsModule),
      },
      {
        path: 'next-of-keen',
        data: { pageTitle: 'NextOfKeens' },
        loadChildren: () => import('./next-of-keen/next-of-keen.module').then(m => m.NextOfKeenModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
