import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoardUser } from '../board-user.model';
import { BoardUserService } from '../service/board-user.service';

@Component({
  templateUrl: './board-user-delete-dialog.component.html',
})
export class BoardUserDeleteDialogComponent {
  boardUser?: IBoardUser;

  constructor(protected boardUserService: BoardUserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.boardUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
