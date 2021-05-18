import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBoardMlmUser, BoardMlmUser } from '../board-mlm-user.model';
import { BoardMlmUserService } from '../service/board-mlm-user.service';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { IMlmUser } from 'app/entities/mlm-user/mlm-user.model';
import { MlmUserService } from 'app/entities/mlm-user/service/mlm-user.service';

@Component({
  selector: 'jhi-board-mlm-user-update',
  templateUrl: './board-mlm-user-update.component.html',
})
export class BoardMlmUserUpdateComponent implements OnInit {
  isSaving = false;

  boardsSharedCollection: IBoard[] = [];
  mlmUsersSharedCollection: IMlmUser[] = [];

  editForm = this.fb.group({
    id: [],
    boardId: [],
    mlmUserId: [],
    level: [],
    boardId: [],
    mlmUserId: [],
  });

  constructor(
    protected boardMlmUserService: BoardMlmUserService,
    protected boardService: BoardService,
    protected mlmUserService: MlmUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardMlmUser }) => {
      this.updateForm(boardMlmUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const boardMlmUser = this.createFromForm();
    if (boardMlmUser.id !== undefined) {
      this.subscribeToSaveResponse(this.boardMlmUserService.update(boardMlmUser));
    } else {
      this.subscribeToSaveResponse(this.boardMlmUserService.create(boardMlmUser));
    }
  }

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
  }

  trackMlmUserById(index: number, item: IMlmUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoardMlmUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(boardMlmUser: IBoardMlmUser): void {
    this.editForm.patchValue({
      id: boardMlmUser.id,
      boardId: boardMlmUser.boardId,
      mlmUserId: boardMlmUser.mlmUserId,
      level: boardMlmUser.level,
      boardId: boardMlmUser.boardId,
      mlmUserId: boardMlmUser.mlmUserId,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, boardMlmUser.boardId);
    this.mlmUsersSharedCollection = this.mlmUserService.addMlmUserToCollectionIfMissing(
      this.mlmUsersSharedCollection,
      boardMlmUser.mlmUserId
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('boardId')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));

    this.mlmUserService
      .query()
      .pipe(map((res: HttpResponse<IMlmUser[]>) => res.body ?? []))
      .pipe(
        map((mlmUsers: IMlmUser[]) => this.mlmUserService.addMlmUserToCollectionIfMissing(mlmUsers, this.editForm.get('mlmUserId')!.value))
      )
      .subscribe((mlmUsers: IMlmUser[]) => (this.mlmUsersSharedCollection = mlmUsers));
  }

  protected createFromForm(): IBoardMlmUser {
    return {
      ...new BoardMlmUser(),
      id: this.editForm.get(['id'])!.value,
      boardId: this.editForm.get(['boardId'])!.value,
      mlmUserId: this.editForm.get(['mlmUserId'])!.value,
      level: this.editForm.get(['level'])!.value,
      boardId: this.editForm.get(['boardId'])!.value,
      mlmUserId: this.editForm.get(['mlmUserId'])!.value,
    };
  }
}
