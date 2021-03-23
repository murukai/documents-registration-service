import { ICountry } from 'app/entities/country/country.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';

export interface IAddress {
  id?: number;
  streetAddress?: string;
  postalCode?: string;
  city?: string;
  stateProvince?: string | null;
  placeOfBirth?: string;
  country?: ICountry;
  countryOfBirth?: ICountry;
  countryOfResidence?: ICountry;
  applicant?: IApplicant | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public streetAddress?: string,
    public postalCode?: string,
    public city?: string,
    public stateProvince?: string | null,
    public placeOfBirth?: string,
    public country?: ICountry,
    public countryOfBirth?: ICountry,
    public countryOfResidence?: ICountry,
    public applicant?: IApplicant | null
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
