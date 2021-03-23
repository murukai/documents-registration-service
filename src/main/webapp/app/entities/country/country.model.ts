export interface ICountry {
  id?: number;
  countryName?: string;
  countryCode?: string;
  callingCode?: string;
  subRegion?: string | null;
  region?: string | null;
  population?: number | null;
  timeZone?: string | null;
  numericCode?: number | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public countryName?: string,
    public countryCode?: string,
    public callingCode?: string,
    public subRegion?: string | null,
    public region?: string | null,
    public population?: number | null,
    public timeZone?: string | null,
    public numericCode?: number | null
  ) {}
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}
