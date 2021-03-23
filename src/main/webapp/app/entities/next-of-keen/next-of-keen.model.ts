import { IApplicant } from 'app/entities/applicant/applicant.model';

export interface INextOfKeen {
  id?: number;
  fullName?: string;
  address?: string;
  cellphone?: string;
  applicant?: IApplicant;
}

export class NextOfKeen implements INextOfKeen {
  constructor(
    public id?: number,
    public fullName?: string,
    public address?: string,
    public cellphone?: string,
    public applicant?: IApplicant
  ) {}
}

export function getNextOfKeenIdentifier(nextOfKeen: INextOfKeen): number | undefined {
  return nextOfKeen.id;
}
