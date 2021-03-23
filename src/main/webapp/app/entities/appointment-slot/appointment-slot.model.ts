import * as dayjs from 'dayjs';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { IAppointment } from 'app/entities/appointment/appointment.model';

export interface IAppointmentSlot {
  id?: number;
  timeOfAppointment?: dayjs.Dayjs;
  available?: boolean;
  maxAppointments?: number;
  applicants?: IApplicant[] | null;
  appointment?: IAppointment | null;
}

export class AppointmentSlot implements IAppointmentSlot {
  constructor(
    public id?: number,
    public timeOfAppointment?: dayjs.Dayjs,
    public available?: boolean,
    public maxAppointments?: number,
    public applicants?: IApplicant[] | null,
    public appointment?: IAppointment | null
  ) {
    this.available = this.available ?? false;
  }
}

export function getAppointmentSlotIdentifier(appointmentSlot: IAppointmentSlot): number | undefined {
  return appointmentSlot.id;
}
