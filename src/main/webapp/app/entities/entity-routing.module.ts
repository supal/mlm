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
        path: 'board-user',
        data: { pageTitle: 'mlmApp.boardUser.home.title' },
        loadChildren: () => import('./board-user/board-user.module').then(m => m.BoardUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
