import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAppointmentSettings, getAppointmentSettingsIdentifier } from '../appointment-settings.model';

export type EntityResponseType = HttpResponse<IAppointmentSettings>;
export type EntityArrayResponseType = HttpResponse<IAppointmentSettings[]>;

@Injectable({ providedIn: 'root' })
export class AppointmentSettingsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/appointment-settings');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/appointment-settings');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(appointmentSettings: IAppointmentSettings): Observable<EntityResponseType> {
    return this.http.post<IAppointmentSettings>(this.resourceUrl, appointmentSettings, { observe: 'response' });
  }

  update(appointmentSettings: IAppointmentSettings): Observable<EntityResponseType> {
    return this.http.put<IAppointmentSettings>(
      `${this.resourceUrl}/${getAppointmentSettingsIdentifier(appointmentSettings) as number}`,
      appointmentSettings,
      { observe: 'response' }
    );
  }

  partialUpdate(appointmentSettings: IAppointmentSettings): Observable<EntityResponseType> {
    return this.http.patch<IAppointmentSettings>(
      `${this.resourceUrl}/${getAppointmentSettingsIdentifier(appointmentSettings) as number}`,
      appointmentSettings,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppointmentSettings>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppointmentSettings[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppointmentSettings[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAppointmentSettingsToCollectionIfMissing(
    appointmentSettingsCollection: IAppointmentSettings[],
    ...appointmentSettingsToCheck: (IAppointmentSettings | null | undefined)[]
  ): IAppointmentSettings[] {
    const appointmentSettings: IAppointmentSettings[] = appointmentSettingsToCheck.filter(isPresent);
    if (appointmentSettings.length > 0) {
      const appointmentSettingsCollectionIdentifiers = appointmentSettingsCollection.map(
        appointmentSettingsItem => getAppointmentSettingsIdentifier(appointmentSettingsItem)!
      );
      const appointmentSettingsToAdd = appointmentSettings.filter(appointmentSettingsItem => {
        const appointmentSettingsIdentifier = getAppointmentSettingsIdentifier(appointmentSettingsItem);
        if (appointmentSettingsIdentifier == null || appointmentSettingsCollectionIdentifiers.includes(appointmentSettingsIdentifier)) {
          return false;
        }
        appointmentSettingsCollectionIdentifiers.push(appointmentSettingsIdentifier);
        return true;
      });
      return [...appointmentSettingsToAdd, ...appointmentSettingsCollection];
    }
    return appointmentSettingsCollection;
  }
}
