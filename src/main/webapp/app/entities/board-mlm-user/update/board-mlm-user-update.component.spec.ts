jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BoardMlmUserService } from '../service/board-mlm-user.service';
import { IBoardMlmUser, BoardMlmUser } from '../board-mlm-user.model';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { IMlmUser } from 'app/entities/mlm-user/mlm-user.model';
import { MlmUserService } from 'app/entities/mlm-user/service/mlm-user.service';

import { BoardMlmUserUpdateComponent } from './board-mlm-user-update.component';

describe('Component Tests', () => {
  describe('BoardMlmUser Management Update Component', () => {
    let comp: BoardMlmUserUpdateComponent;
    let fixture: ComponentFixture<BoardMlmUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let boardMlmUserService: BoardMlmUserService;
    let boardService: BoardService;
    let mlmUserService: MlmUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardMlmUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BoardMlmUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardMlmUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      boardMlmUserService = TestBed.inject(BoardMlmUserService);
      boardService = TestBed.inject(BoardService);
      mlmUserService = TestBed.inject(MlmUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Board query and add missing value', () => {
        const boardMlmUser: IBoardMlmUser = { id: 456 };
        const board: IBoard = { id: 65 };
        boardMlmUser.board = board;

        const boardCollection: IBoard[] = [{ id: 18437 }];
        spyOn(boardService, 'query').and.returnValue(of(new HttpResponse({ body: boardCollection })));
        const additionalBoards = [board];
        const expectedCollection: IBoard[] = [...additionalBoards, ...boardCollection];
        spyOn(boardService, 'addBoardToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ boardMlmUser });
        comp.ngOnInit();

        expect(boardService.query).toHaveBeenCalled();
        expect(boardService.addBoardToCollectionIfMissing).toHaveBeenCalledWith(boardCollection, ...additionalBoards);
        expect(comp.boardsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call MlmUser query and add missing value', () => {
        const boardMlmUser: IBoardMlmUser = { id: 456 };
        const mlmUser: IMlmUser = { id: 2905 };
        boardMlmUser.mlmUser = mlmUser;

        const mlmUserCollection: IMlmUser[] = [{ id: 93710 }];
        spyOn(mlmUserService, 'query').and.returnValue(of(new HttpResponse({ body: mlmUserCollection })));
        const additionalMlmUsers = [mlmUser];
        const expectedCollection: IMlmUser[] = [...additionalMlmUsers, ...mlmUserCollection];
        spyOn(mlmUserService, 'addMlmUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ boardMlmUser });
        comp.ngOnInit();

        expect(mlmUserService.query).toHaveBeenCalled();
        expect(mlmUserService.addMlmUserToCollectionIfMissing).toHaveBeenCalledWith(mlmUserCollection, ...additionalMlmUsers);
        expect(comp.mlmUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const boardMlmUser: IBoardMlmUser = { id: 456 };
        const board: IBoard = { id: 64713 };
        boardMlmUser.board = board;
        const mlmUser: IMlmUser = { id: 52550 };
        boardMlmUser.mlmUser = mlmUser;

        activatedRoute.data = of({ boardMlmUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(boardMlmUser));
        expect(comp.boardsSharedCollection).toContain(board);
        expect(comp.mlmUsersSharedCollection).toContain(mlmUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardMlmUser = { id: 123 };
        spyOn(boardMlmUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardMlmUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardMlmUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(boardMlmUserService.update).toHaveBeenCalledWith(boardMlmUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardMlmUser = new BoardMlmUser();
        spyOn(boardMlmUserService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardMlmUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardMlmUser }));
        saveSubject.complete();

        // THEN
        expect(boardMlmUserService.create).toHaveBeenCalledWith(boardMlmUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardMlmUser = { id: 123 };
        spyOn(boardMlmUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardMlmUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(boardMlmUserService.update).toHaveBeenCalledWith(boardMlmUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBoardById', () => {
        it('Should return tracked Board primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBoardById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMlmUserById', () => {
        it('Should return tracked MlmUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMlmUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
