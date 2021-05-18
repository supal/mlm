import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MlmUserComponent } from './list/mlm-user.component';
import { MlmUserDetailComponent } from './detail/mlm-user-detail.component';
import { MlmUserUpdateComponent } from './update/mlm-user-update.component';
import { MlmUserDeleteDialogComponent } from './delete/mlm-user-delete-dialog.component';
import { MlmUserRoutingModule } from './route/mlm-user-routing.module';

@NgModule({
  imports: [SharedModule, MlmUserRoutingModule],
  declarations: [MlmUserComponent, MlmUserDetailComponent, MlmUserUpdateComponent, MlmUserDeleteDialogComponent],
  entryComponents: [MlmUserDeleteDialogComponent],
})
export class MlmUserModule {}
