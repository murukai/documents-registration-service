import * as dayjs from 'dayjs';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { AppointmentType } from 'app/entities/enumerations/appointment-type.model';

export interface IAppointment {
  id?: number;
  dateOfAppointment?: dayjs.Dayjs;
  available?: boolean;
  appointmentType?: AppointmentType | null;
  maxAppointments?: number;
  appointmentSlots?: IAppointmentSlot[] | null;
}

export class Appointment implements IAppointment {
  constructor(
    public id?: number,
    public dateOfAppointment?: dayjs.Dayjs,
    public available?: boolean,
    public appointmentType?: AppointmentType | null,
    public maxAppointments?: number,
    public appointmentSlots?: IAppointmentSlot[] | null
  ) {
    this.available = this.available ?? false;
  }
}

export function getAppointmentIdentifier(appointment: IAppointment): number | undefined {
  return appointment.id;
}
