import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoardUserComponent } from '../list/board-user.component';
import { BoardUserDetailComponent } from '../detail/board-user-detail.component';
import { BoardUserUpdateComponent } from '../update/board-user-update.component';
import { BoardUserRoutingResolveService } from './board-user-routing-resolve.service';

const boardUserRoute: Routes = [
  {
    path: '',
    component: BoardUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BoardUserDetailComponent,
    resolve: {
      boardUser: BoardUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BoardUserUpdateComponent,
    resolve: {
      boardUser: BoardUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BoardUserUpdateComponent,
    resolve: {
      boardUser: BoardUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(boardUserRoute)],
  exports: [RouterModule],
})
export class BoardUserRoutingModule {}
