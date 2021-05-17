import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoardUser, BoardUser } from '../board-user.model';
import { BoardUserService } from '../service/board-user.service';

@Injectable({ providedIn: 'root' })
export class BoardUserRoutingResolveService implements Resolve<IBoardUser> {
  constructor(protected service: BoardUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBoardUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((boardUser: HttpResponse<BoardUser>) => {
          if (boardUser.body) {
            return of(boardUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BoardUser());
  }
}
