import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAppointmentSlot, getAppointmentSlotIdentifier } from '../appointment-slot.model';

export type EntityResponseType = HttpResponse<IAppointmentSlot>;
export type EntityArrayResponseType = HttpResponse<IAppointmentSlot[]>;

@Injectable({ providedIn: 'root' })
export class AppointmentSlotService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/appointment-slots');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/appointment-slots');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(appointmentSlot: IAppointmentSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentSlot);
    return this.http
      .post<IAppointmentSlot>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appointmentSlot: IAppointmentSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentSlot);
    return this.http
      .put<IAppointmentSlot>(`${this.resourceUrl}/${getAppointmentSlotIdentifier(appointmentSlot) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appointmentSlot: IAppointmentSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentSlot);
    return this.http
      .patch<IAppointmentSlot>(`${this.resourceUrl}/${getAppointmentSlotIdentifier(appointmentSlot) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppointmentSlot>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppointmentSlot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppointmentSlot[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAppointmentSlotToCollectionIfMissing(
    appointmentSlotCollection: IAppointmentSlot[],
    ...appointmentSlotsToCheck: (IAppointmentSlot | null | undefined)[]
  ): IAppointmentSlot[] {
    const appointmentSlots: IAppointmentSlot[] = appointmentSlotsToCheck.filter(isPresent);
    if (appointmentSlots.length > 0) {
      const appointmentSlotCollectionIdentifiers = appointmentSlotCollection.map(
        appointmentSlotItem => getAppointmentSlotIdentifier(appointmentSlotItem)!
      );
      const appointmentSlotsToAdd = appointmentSlots.filter(appointmentSlotItem => {
        const appointmentSlotIdentifier = getAppointmentSlotIdentifier(appointmentSlotItem);
        if (appointmentSlotIdentifier == null || appointmentSlotCollectionIdentifiers.includes(appointmentSlotIdentifier)) {
          return false;
        }
        appointmentSlotCollectionIdentifiers.push(appointmentSlotIdentifier);
        return true;
      });
      return [...appointmentSlotsToAdd, ...appointmentSlotCollection];
    }
    return appointmentSlotCollection;
  }

  protected convertDateFromClient(appointmentSlot: IAppointmentSlot): IAppointmentSlot {
    return Object.assign({}, appointmentSlot, {
      timeOfAppointment: appointmentSlot.timeOfAppointment?.isValid() ? appointmentSlot.timeOfAppointment.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfAppointment = res.body.timeOfAppointment ? dayjs(res.body.timeOfAppointment) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appointmentSlot: IAppointmentSlot) => {
        appointmentSlot.timeOfAppointment = appointmentSlot.timeOfAppointment ? dayjs(appointmentSlot.timeOfAppointment) : undefined;
      });
    }
    return res;
  }
}
