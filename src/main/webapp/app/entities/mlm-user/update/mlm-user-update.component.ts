import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMlmUser, MlmUser } from '../mlm-user.model';
import { MlmUserService } from '../service/mlm-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-mlm-user-update',
  templateUrl: './mlm-user-update.component.html',
})
export class MlmUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    userId: [],
    paid: [],
    active: [],
    refMlmUserId: [],
    userId: [],
  });

  constructor(
    protected mlmUserService: MlmUserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mlmUser }) => {
      this.updateForm(mlmUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mlmUser = this.createFromForm();
    if (mlmUser.id !== undefined) {
      this.subscribeToSaveResponse(this.mlmUserService.update(mlmUser));
    } else {
      this.subscribeToSaveResponse(this.mlmUserService.create(mlmUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMlmUser>>): void {
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

  protected updateForm(mlmUser: IMlmUser): void {
    this.editForm.patchValue({
      id: mlmUser.id,
      userId: mlmUser.userId,
      paid: mlmUser.paid,
      active: mlmUser.active,
      refMlmUserId: mlmUser.refMlmUserId,
      userId: mlmUser.userId,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, mlmUser.userId);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('userId')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IMlmUser {
    return {
      ...new MlmUser(),
      id: this.editForm.get(['id'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      paid: this.editForm.get(['paid'])!.value,
      active: this.editForm.get(['active'])!.value,
      refMlmUserId: this.editForm.get(['refMlmUserId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }
}
