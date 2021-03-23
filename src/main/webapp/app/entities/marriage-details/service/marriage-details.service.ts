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
import { IMarriageDetails, getMarriageDetailsIdentifier } from '../marriage-details.model';

export type EntityResponseType = HttpResponse<IMarriageDetails>;
export type EntityArrayResponseType = HttpResponse<IMarriageDetails[]>;

@Injectable({ providedIn: 'root' })
export class MarriageDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/marriage-details');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/marriage-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(marriageDetails: IMarriageDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(marriageDetails);
    return this.http
      .post<IMarriageDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(marriageDetails: IMarriageDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(marriageDetails);
    return this.http
      .put<IMarriageDetails>(`${this.resourceUrl}/${getMarriageDetailsIdentifier(marriageDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(marriageDetails: IMarriageDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(marriageDetails);
    return this.http
      .patch<IMarriageDetails>(`${this.resourceUrl}/${getMarriageDetailsIdentifier(marriageDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMarriageDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMarriageDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMarriageDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addMarriageDetailsToCollectionIfMissing(
    marriageDetailsCollection: IMarriageDetails[],
    ...marriageDetailsToCheck: (IMarriageDetails | null | undefined)[]
  ): IMarriageDetails[] {
    const marriageDetails: IMarriageDetails[] = marriageDetailsToCheck.filter(isPresent);
    if (marriageDetails.length > 0) {
      const marriageDetailsCollectionIdentifiers = marriageDetailsCollection.map(
        marriageDetailsItem => getMarriageDetailsIdentifier(marriageDetailsItem)!
      );
      const marriageDetailsToAdd = marriageDetails.filter(marriageDetailsItem => {
        const marriageDetailsIdentifier = getMarriageDetailsIdentifier(marriageDetailsItem);
        if (marriageDetailsIdentifier == null || marriageDetailsCollectionIdentifiers.includes(marriageDetailsIdentifier)) {
          return false;
        }
        marriageDetailsCollectionIdentifiers.push(marriageDetailsIdentifier);
        return true;
      });
      return [...marriageDetailsToAdd, ...marriageDetailsCollection];
    }
    return marriageDetailsCollection;
  }

  protected convertDateFromClient(marriageDetails: IMarriageDetails): IMarriageDetails {
    return Object.assign({}, marriageDetails, {
      dateOfMarriage: marriageDetails.dateOfMarriage?.isValid() ? marriageDetails.dateOfMarriage.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfMarriage = res.body.dateOfMarriage ? dayjs(res.body.dateOfMarriage) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((marriageDetails: IMarriageDetails) => {
        marriageDetails.dateOfMarriage = marriageDetails.dateOfMarriage ? dayjs(marriageDetails.dateOfMarriage) : undefined;
      });
    }
    return res;
  }
}
