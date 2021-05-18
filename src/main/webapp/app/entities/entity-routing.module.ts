import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'board',
        data: { pageTitle: 'mlmApp.board.home.title' },
        loadChildren: () => import('./board/board.module').then(m => m.BoardModule),
      },
      {
        path: 'board-mlm-user',
        data: { pageTitle: 'mlmApp.boardMlmUser.home.title' },
        loadChildren: () => import('./board-mlm-user/board-mlm-user.module').then(m => m.BoardMlmUserModule),
      },
      {
        path: 'mlm-user',
        data: { pageTitle: 'mlmApp.mlmUser.home.title' },
        loadChildren: () => import('./mlm-user/mlm-user.module').then(m => m.MlmUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
