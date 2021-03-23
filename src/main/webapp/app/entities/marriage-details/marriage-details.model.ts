import * as dayjs from 'dayjs';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ICountry } from 'app/entities/country/country.model';

export interface IMarriageDetails {
  id?: number;
  dateOfMarriage?: dayjs.Dayjs;
  spouseFullName?: string;
  placeOfMarriage?: string;
  spousePlaceOfBirth?: string;
  marriageNumber?: string;
  marriedBefore?: boolean;
  applicant?: IApplicant;
  countryOfMarriage?: ICountry;
  spouseCountryOfBirth?: ICountry;
}

export class MarriageDetails implements IMarriageDetails {
  constructor(
    public id?: number,
    public dateOfMarriage?: dayjs.Dayjs,
    public spouseFullName?: string,
    public placeOfMarriage?: string,
    public spousePlaceOfBirth?: string,
    public marriageNumber?: string,
    public marriedBefore?: boolean,
    public applicant?: IApplicant,
    public countryOfMarriage?: ICountry,
    public spouseCountryOfBirth?: ICountry
  ) {
    this.marriedBefore = this.marriedBefore ?? false;
  }
}

export function getMarriageDetailsIdentifier(marriageDetails: IMarriageDetails): number | undefined {
  return marriageDetails.id;
}
