import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMlmUser } from '../mlm-user.model';

@Component({
  selector: 'jhi-mlm-user-detail',
  templateUrl: './mlm-user-detail.component.html',
})
export class MlmUserDetailComponent implements OnInit {
  mlmUser: IMlmUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mlmUser }) => {
      this.mlmUser = mlmUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
