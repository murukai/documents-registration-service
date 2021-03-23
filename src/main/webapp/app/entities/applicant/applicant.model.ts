import * as dayjs from 'dayjs';
import { IAddress } from 'app/entities/address/address.model';
import { IUser } from 'app/entities/user/user.model';
import { IMarriageDetails } from 'app/entities/marriage-details/marriage-details.model';
import { INextOfKeen } from 'app/entities/next-of-keen/next-of-keen.model';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { EyeColor } from 'app/entities/enumerations/eye-color.model';
import { HairColor } from 'app/entities/enumerations/hair-color.model';

export interface IApplicant {
  id?: number;
  firstNames?: string;
  lastName?: string;
  initials?: string;
  gender?: Gender | null;
  email?: string;
  maritalStatus?: MaritalStatus | null;
  dateOfBirth?: dayjs.Dayjs;
  idNumber?: string;
  birthEntryNumber?: string;
  eyeColor?: EyeColor;
  hairColor?: HairColor;
  visibleMarks?: string;
  profession?: string;
  imageContentType?: string | null;
  image?: string | null;
  addresses?: IAddress[] | null;
  user?: IUser;
  marriageDetails?: IMarriageDetails | null;
  nextOfKeen?: INextOfKeen | null;
  appointmentSlot?: IAppointmentSlot | null;
}

export class Applicant implements IApplicant {
  constructor(
    public id?: number,
    public firstNames?: string,
    public lastName?: string,
    public initials?: string,
    public gender?: Gender | null,
    public email?: string,
    public maritalStatus?: MaritalStatus | null,
    public dateOfBirth?: dayjs.Dayjs,
    public idNumber?: string,
    public birthEntryNumber?: string,
    public eyeColor?: EyeColor,
    public hairColor?: HairColor,
    public visibleMarks?: string,
    public profession?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public addresses?: IAddress[] | null,
    public user?: IUser,
    public marriageDetails?: IMarriageDetails | null,
    public nextOfKeen?: INextOfKeen | null,
    public appointmentSlot?: IAppointmentSlot | null
  ) {}
}

export function getApplicantIdentifier(applicant: IApplicant): number | undefined {
  return applicant.id;
}
