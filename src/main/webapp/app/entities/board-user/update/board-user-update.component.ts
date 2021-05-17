import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBoardUser, BoardUser } from '../board-user.model';
import { BoardUserService } from '../service/board-user.service';

@Component({
  selector: 'jhi-board-user-update',
  templateUrl: './board-user-update.component.html',
})
export class BoardUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    boardId: [],
    userId: [],
    level: [],
    status: [],
  });

  constructor(protected boardUserService: BoardUserService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUser }) => {
      this.updateForm(boardUser);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const boardUser = this.createFromForm();
    if (boardUser.id !== undefined) {
      this.subscribeToSaveResponse(this.boardUserService.update(boardUser));
    } else {
      this.subscribeToSaveResponse(this.boardUserService.create(boardUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoardUser>>): void {
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

  protected updateForm(boardUser: IBoardUser): void {
    this.editForm.patchValue({
      id: boardUser.id,
      boardId: boardUser.boardId,
      userId: boardUser.userId,
      level: boardUser.level,
      status: boardUser.status,
    });
  }

  protected createFromForm(): IBoardUser {
    return {
      ...new BoardUser(),
      id: this.editForm.get(['id'])!.value,
      boardId: this.editForm.get(['boardId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      level: this.editForm.get(['level'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
