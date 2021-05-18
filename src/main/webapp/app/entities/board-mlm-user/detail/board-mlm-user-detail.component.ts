import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoardMlmUser } from '../board-mlm-user.model';

@Component({
  selector: 'jhi-board-mlm-user-detail',
  templateUrl: './board-mlm-user-detail.component.html',
})
export class BoardMlmUserDetailComponent implements OnInit {
  boardMlmUser: IBoardMlmUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardMlmUser }) => {
      this.boardMlmUser = boardMlmUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
