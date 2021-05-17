import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BoardUserComponent } from './list/board-user.component';
import { BoardUserDetailComponent } from './detail/board-user-detail.component';
import { BoardUserUpdateComponent } from './update/board-user-update.component';
import { BoardUserDeleteDialogComponent } from './delete/board-user-delete-dialog.component';
import { BoardUserRoutingModule } from './route/board-user-routing.module';

@NgModule({
  imports: [SharedModule, BoardUserRoutingModule],
  declarations: [BoardUserComponent, BoardUserDetailComponent, BoardUserUpdateComponent, BoardUserDeleteDialogComponent],
  entryComponents: [BoardUserDeleteDialogComponent],
})
export class BoardUserModule {}
