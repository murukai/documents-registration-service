export interface IAppointmentSettings {
  id?: number;
  maxOrdinaryAppointments?: number;
  maxEmergencyAppointments?: number;
  startingWorkTime?: number;
  endingWorkTime?: number;
}

export class AppointmentSettings implements IAppointmentSettings {
  constructor(
    public id?: number,
    public maxOrdinaryAppointments?: number,
    public maxEmergencyAppointments?: number,
    public startingWorkTime?: number,
    public endingWorkTime?: number
  ) {}
}

export function getAppointmentSettingsIdentifier(appointmentSettings: IAppointmentSettings): number | undefined {
  return appointmentSettings.id;
}
