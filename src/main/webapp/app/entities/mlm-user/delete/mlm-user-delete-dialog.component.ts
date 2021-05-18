import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMlmUser } from '../mlm-user.model';
import { MlmUserService } from '../service/mlm-user.service';

@Component({
  templateUrl: './mlm-user-delete-dialog.component.html',
})
export class MlmUserDeleteDialogComponent {
  mlmUser?: IMlmUser;

  constructor(protected mlmUserService: MlmUserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mlmUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
