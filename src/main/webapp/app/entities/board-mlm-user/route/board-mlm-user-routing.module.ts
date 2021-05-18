import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoardMlmUserComponent } from '../list/board-mlm-user.component';
import { BoardMlmUserDetailComponent } from '../detail/board-mlm-user-detail.component';
import { BoardMlmUserUpdateComponent } from '../update/board-mlm-user-update.component';
import { BoardMlmUserRoutingResolveService } from './board-mlm-user-routing-resolve.service';

const boardMlmUserRoute: Routes = [
  {
    path: '',
    component: BoardMlmUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BoardMlmUserDetailComponent,
    resolve: {
      boardMlmUser: BoardMlmUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BoardMlmUserUpdateComponent,
    resolve: {
      boardMlmUser: BoardMlmUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BoardMlmUserUpdateComponent,
    resolve: {
      boardMlmUser: BoardMlmUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(boardMlmUserRoute)],
  exports: [RouterModule],
})
export class BoardMlmUserRoutingModule {}
