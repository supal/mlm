import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBoard, Board } from '../board.model';
import { BoardService } from '../service/board.service';

@Component({
  selector: 'jhi-board-update',
  templateUrl: './board-update.component.html',
})
export class BoardUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    boardId: [],
    type: [null, [Validators.required]],
    status: [],
  });

  constructor(protected boardService: BoardService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ board }) => {
      this.updateForm(board);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const board = this.createFromForm();
    if (board.boardId !== undefined) {
      this.subscribeToSaveResponse(this.boardService.update(board));
    } else {
      this.subscribeToSaveResponse(this.boardService.create(board));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoard>>): void {
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

  protected updateForm(board: IBoard): void {
    this.editForm.patchValue({
      boardId: board.boardId,
      type: board.type,
      status: board.status,
    });
  }

  protected createFromForm(): IBoard {
    return {
      ...new Board(),
      boardId: this.editForm.get(['boardId'])!.value,
      type: this.editForm.get(['type'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
