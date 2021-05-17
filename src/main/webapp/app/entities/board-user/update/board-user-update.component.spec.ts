jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BoardUserService } from '../service/board-user.service';
import { IBoardUser, BoardUser } from '../board-user.model';

import { BoardUserUpdateComponent } from './board-user-update.component';

describe('Component Tests', () => {
  describe('BoardUser Management Update Component', () => {
    let comp: BoardUserUpdateComponent;
    let fixture: ComponentFixture<BoardUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let boardUserService: BoardUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BoardUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      boardUserService = TestBed.inject(BoardUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const boardUser: IBoardUser = { id: 456 };

        activatedRoute.data = of({ boardUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(boardUser));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUser = { id: 123 };
        spyOn(boardUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(boardUserService.update).toHaveBeenCalledWith(boardUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUser = new BoardUser();
        spyOn(boardUserService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUser }));
        saveSubject.complete();

        // THEN
        expect(boardUserService.create).toHaveBeenCalledWith(boardUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUser = { id: 123 };
        spyOn(boardUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(boardUserService.update).toHaveBeenCalledWith(boardUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
