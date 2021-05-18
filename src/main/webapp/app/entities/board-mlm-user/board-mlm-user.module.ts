import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BoardMlmUserComponent } from './list/board-mlm-user.component';
import { BoardMlmUserDetailComponent } from './detail/board-mlm-user-detail.component';
import { BoardMlmUserUpdateComponent } from './update/board-mlm-user-update.component';
import { BoardMlmUserDeleteDialogComponent } from './delete/board-mlm-user-delete-dialog.component';
import { BoardMlmUserRoutingModule } from './route/board-mlm-user-routing.module';

@NgModule({
  imports: [SharedModule, BoardMlmUserRoutingModule],
  declarations: [BoardMlmUserComponent, BoardMlmUserDetailComponent, BoardMlmUserUpdateComponent, BoardMlmUserDeleteDialogComponent],
  entryComponents: [BoardMlmUserDeleteDialogComponent],
})
export class BoardMlmUserModule {}
