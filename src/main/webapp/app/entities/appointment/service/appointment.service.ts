import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAppointment, getAppointmentIdentifier } from '../appointment.model';

export type EntityResponseType = HttpResponse<IAppointment>;
export type EntityArrayResponseType = HttpResponse<IAppointment[]>;

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/appointments');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/appointments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(appointment: IAppointment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointment);
    return this.http
      .post<IAppointment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appointment: IAppointment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointment);
    return this.http
      .put<IAppointment>(`${this.resourceUrl}/${getAppointmentIdentifier(appointment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appointment: IAppointment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointment);
    return this.http
      .patch<IAppointment>(`${this.resourceUrl}/${getAppointmentIdentifier(appointment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppointment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppointment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppointment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAppointmentToCollectionIfMissing(
    appointmentCollection: IAppointment[],
    ...appointmentsToCheck: (IAppointment | null | undefined)[]
  ): IAppointment[] {
    const appointments: IAppointment[] = appointmentsToCheck.filter(isPresent);
    if (appointments.length > 0) {
      const appointmentCollectionIdentifiers = appointmentCollection.map(appointmentItem => getAppointmentIdentifier(appointmentItem)!);
      const appointmentsToAdd = appointments.filter(appointmentItem => {
        const appointmentIdentifier = getAppointmentIdentifier(appointmentItem);
        if (appointmentIdentifier == null || appointmentCollectionIdentifiers.includes(appointmentIdentifier)) {
          return false;
        }
        appointmentCollectionIdentifiers.push(appointmentIdentifier);
        return true;
      });
      return [...appointmentsToAdd, ...appointmentCollection];
    }
    return appointmentCollection;
  }

  protected convertDateFromClient(appointment: IAppointment): IAppointment {
    return Object.assign({}, appointment, {
      dateOfAppointment: appointment.dateOfAppointment?.isValid() ? appointment.dateOfAppointment.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfAppointment = res.body.dateOfAppointment ? dayjs(res.body.dateOfAppointment) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appointment: IAppointment) => {
        appointment.dateOfAppointment = appointment.dateOfAppointment ? dayjs(appointment.dateOfAppointment) : undefined;
      });
    }
    return res;
  }
}
