import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IApplicant, getApplicantIdentifier } from '../applicant.model';

export type EntityResponseType = HttpResponse<IApplicant>;
export type EntityArrayResponseType = HttpResponse<IApplicant[]>;

@Injectable({ providedIn: 'root' })
export class ApplicantService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/applicants');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/applicants');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(applicant: IApplicant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicant);
    return this.http
      .post<IApplicant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(applicant: IApplicant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicant);
    return this.http
      .put<IApplicant>(`${this.resourceUrl}/${getApplicantIdentifier(applicant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(applicant: IApplicant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicant);
    return this.http
      .patch<IApplicant>(`${this.resourceUrl}/${getApplicantIdentifier(applicant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApplicant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApplicant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApplicant[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addApplicantToCollectionIfMissing(
    applicantCollection: IApplicant[],
    ...applicantsToCheck: (IApplicant | null | undefined)[]
  ): IApplicant[] {
    const applicants: IApplicant[] = applicantsToCheck.filter(isPresent);
    if (applicants.length > 0) {
      const applicantCollectionIdentifiers = applicantCollection.map(applicantItem => getApplicantIdentifier(applicantItem)!);
      const applicantsToAdd = applicants.filter(applicantItem => {
        const applicantIdentifier = getApplicantIdentifier(applicantItem);
        if (applicantIdentifier == null || applicantCollectionIdentifiers.includes(applicantIdentifier)) {
          return false;
        }
        applicantCollectionIdentifiers.push(applicantIdentifier);
        return true;
      });
      return [...applicantsToAdd, ...applicantCollection];
    }
    return applicantCollection;
  }

  protected convertDateFromClient(applicant: IApplicant): IApplicant {
    return Object.assign({}, applicant, {
      dateOfBirth: applicant.dateOfBirth?.isValid() ? applicant.dateOfBirth.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((applicant: IApplicant) => {
        applicant.dateOfBirth = applicant.dateOfBirth ? dayjs(applicant.dateOfBirth) : undefined;
      });
    }
    return res;
  }
}
