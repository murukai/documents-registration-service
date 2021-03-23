jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HolidayService } from '../service/holiday.service';
import { IHoliday, Holiday } from '../holiday.model';

import { HolidayUpdateComponent } from './holiday-update.component';

describe('Component Tests', () => {
  describe('Holiday Management Update Component', () => {
    let comp: HolidayUpdateComponent;
    let fixture: ComponentFixture<HolidayUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let holidayService: HolidayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HolidayUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(HolidayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HolidayUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      holidayService = TestBed.inject(HolidayService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const holiday: IHoliday = { id: 456 };

        activatedRoute.data = of({ holiday });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(holiday));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const holiday = { id: 123 };
        spyOn(holidayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ holiday });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: holiday }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(holidayService.update).toHaveBeenCalledWith(holiday);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const holiday = new Holiday();
        spyOn(holidayService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ holiday });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: holiday }));
        saveSubject.complete();

        // THEN
        expect(holidayService.create).toHaveBeenCalledWith(holiday);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const holiday = { id: 123 };
        spyOn(holidayService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ holiday });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(holidayService.update).toHaveBeenCalledWith(holiday);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
