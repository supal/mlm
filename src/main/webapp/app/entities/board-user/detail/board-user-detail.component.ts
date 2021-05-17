import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoardUser } from '../board-user.model';

@Component({
  selector: 'jhi-board-user-detail',
  templateUrl: './board-user-detail.component.html',
})
export class BoardUserDetailComponent implements OnInit {
  boardUser: IBoardUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUser }) => {
      this.boardUser = boardUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
