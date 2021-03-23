import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { INextOfKeen, getNextOfKeenIdentifier } from '../next-of-keen.model';

export type EntityResponseType = HttpResponse<INextOfKeen>;
export type EntityArrayResponseType = HttpResponse<INextOfKeen[]>;

@Injectable({ providedIn: 'root' })
export class NextOfKeenService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/next-of-keens');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/next-of-keens');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(nextOfKeen: INextOfKeen): Observable<EntityResponseType> {
    return this.http.post<INextOfKeen>(this.resourceUrl, nextOfKeen, { observe: 'response' });
  }

  update(nextOfKeen: INextOfKeen): Observable<EntityResponseType> {
    return this.http.put<INextOfKeen>(`${this.resourceUrl}/${getNextOfKeenIdentifier(nextOfKeen) as number}`, nextOfKeen, {
      observe: 'response',
    });
  }

  partialUpdate(nextOfKeen: INextOfKeen): Observable<EntityResponseType> {
    return this.http.patch<INextOfKeen>(`${this.resourceUrl}/${getNextOfKeenIdentifier(nextOfKeen) as number}`, nextOfKeen, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INextOfKeen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INextOfKeen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INextOfKeen[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addNextOfKeenToCollectionIfMissing(
    nextOfKeenCollection: INextOfKeen[],
    ...nextOfKeensToCheck: (INextOfKeen | null | undefined)[]
  ): INextOfKeen[] {
    const nextOfKeens: INextOfKeen[] = nextOfKeensToCheck.filter(isPresent);
    if (nextOfKeens.length > 0) {
      const nextOfKeenCollectionIdentifiers = nextOfKeenCollection.map(nextOfKeenItem => getNextOfKeenIdentifier(nextOfKeenItem)!);
      const nextOfKeensToAdd = nextOfKeens.filter(nextOfKeenItem => {
        const nextOfKeenIdentifier = getNextOfKeenIdentifier(nextOfKeenItem);
        if (nextOfKeenIdentifier == null || nextOfKeenCollectionIdentifiers.includes(nextOfKeenIdentifier)) {
          return false;
        }
        nextOfKeenCollectionIdentifiers.push(nextOfKeenIdentifier);
        return true;
      });
      return [...nextOfKeensToAdd, ...nextOfKeenCollection];
    }
    return nextOfKeenCollection;
  }
}
