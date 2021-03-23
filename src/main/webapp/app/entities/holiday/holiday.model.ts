import { HolidayMonth } from 'app/entities/enumerations/holiday-month.model';

export interface IHoliday {
  id?: number;
  name?: string;
  month?: HolidayMonth;
  day?: number;
}

export class Holiday implements IHoliday {
  constructor(public id?: number, public name?: string, public month?: HolidayMonth, public day?: number) {}
}

export function getHolidayIdentifier(holiday: IHoliday): number | undefined {
  return holiday.id;
}
