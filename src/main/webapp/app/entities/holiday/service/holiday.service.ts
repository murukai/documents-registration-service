import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IHoliday, getHolidayIdentifier } from '../holiday.model';

export type EntityResponseType = HttpResponse<IHoliday>;
export type EntityArrayResponseType = HttpResponse<IHoliday[]>;

@Injectable({ providedIn: 'root' })
export class HolidayService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/holidays');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/holidays');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(holiday: IHoliday): Observable<EntityResponseType> {
    return this.http.post<IHoliday>(this.resourceUrl, holiday, { observe: 'response' });
  }

  update(holiday: IHoliday): Observable<EntityResponseType> {
    return this.http.put<IHoliday>(`${this.resourceUrl}/${getHolidayIdentifier(holiday) as number}`, holiday, { observe: 'response' });
  }

  partialUpdate(holiday: IHoliday): Observable<EntityResponseType> {
    return this.http.patch<IHoliday>(`${this.resourceUrl}/${getHolidayIdentifier(holiday) as number}`, holiday, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHoliday>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHoliday[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHoliday[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addHolidayToCollectionIfMissing(holidayCollection: IHoliday[], ...holidaysToCheck: (IHoliday | null | undefined)[]): IHoliday[] {
    const holidays: IHoliday[] = holidaysToCheck.filter(isPresent);
    if (holidays.length > 0) {
      const holidayCollectionIdentifiers = holidayCollection.map(holidayItem => getHolidayIdentifier(holidayItem)!);
      const holidaysToAdd = holidays.filter(holidayItem => {
        const holidayIdentifier = getHolidayIdentifier(holidayItem);
        if (holidayIdentifier == null || holidayCollectionIdentifiers.includes(holidayIdentifier)) {
          return false;
        }
        holidayCollectionIdentifiers.push(holidayIdentifier);
        return true;
      });
      return [...holidaysToAdd, ...holidayCollection];
    }
    return holidayCollection;
  }
}
