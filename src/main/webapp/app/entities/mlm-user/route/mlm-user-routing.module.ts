import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MlmUserComponent } from '../list/mlm-user.component';
import { MlmUserDetailComponent } from '../detail/mlm-user-detail.component';
import { MlmUserUpdateComponent } from '../update/mlm-user-update.component';
import { MlmUserRoutingResolveService } from './mlm-user-routing-resolve.service';

const mlmUserRoute: Routes = [
  {
    path: '',
    component: MlmUserComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MlmUserDetailComponent,
    resolve: {
      mlmUser: MlmUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MlmUserUpdateComponent,
    resolve: {
      mlmUser: MlmUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MlmUserUpdateComponent,
    resolve: {
      mlmUser: MlmUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mlmUserRoute)],
  exports: [RouterModule],
})
export class MlmUserRoutingModule {}
