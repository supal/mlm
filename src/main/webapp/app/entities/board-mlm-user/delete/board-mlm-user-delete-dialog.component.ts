import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoardMlmUser } from '../board-mlm-user.model';
import { BoardMlmUserService } from '../service/board-mlm-user.service';

@Component({
  templateUrl: './board-mlm-user-delete-dialog.component.html',
})
export class BoardMlmUserDeleteDialogComponent {
  boardMlmUser?: IBoardMlmUser;

  constructor(protected boardMlmUserService: BoardMlmUserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.boardMlmUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
