import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoardMlmUser, getBoardMlmUserIdentifier } from '../board-mlm-user.model';

export type EntityResponseType = HttpResponse<IBoardMlmUser>;
export type EntityArrayResponseType = HttpResponse<IBoardMlmUser[]>;

@Injectable({ providedIn: 'root' })
export class BoardMlmUserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/board-mlm-users');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(boardMlmUser: IBoardMlmUser): Observable<EntityResponseType> {
    return this.http.post<IBoardMlmUser>(this.resourceUrl, boardMlmUser, { observe: 'response' });
  }

  update(boardMlmUser: IBoardMlmUser): Observable<EntityResponseType> {
    return this.http.put<IBoardMlmUser>(`${this.resourceUrl}/${getBoardMlmUserIdentifier(boardMlmUser) as number}`, boardMlmUser, {
      observe: 'response',
    });
  }

  partialUpdate(boardMlmUser: IBoardMlmUser): Observable<EntityResponseType> {
    return this.http.patch<IBoardMlmUser>(`${this.resourceUrl}/${getBoardMlmUserIdentifier(boardMlmUser) as number}`, boardMlmUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoardMlmUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoardMlmUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBoardMlmUserToCollectionIfMissing(
    boardMlmUserCollection: IBoardMlmUser[],
    ...boardMlmUsersToCheck: (IBoardMlmUser | null | undefined)[]
  ): IBoardMlmUser[] {
    const boardMlmUsers: IBoardMlmUser[] = boardMlmUsersToCheck.filter(isPresent);
    if (boardMlmUsers.length > 0) {
      const boardMlmUserCollectionIdentifiers = boardMlmUserCollection.map(
        boardMlmUserItem => getBoardMlmUserIdentifier(boardMlmUserItem)!
      );
      const boardMlmUsersToAdd = boardMlmUsers.filter(boardMlmUserItem => {
        const boardMlmUserIdentifier = getBoardMlmUserIdentifier(boardMlmUserItem);
        if (boardMlmUserIdentifier == null || boardMlmUserCollectionIdentifiers.includes(boardMlmUserIdentifier)) {
          return false;
        }
        boardMlmUserCollectionIdentifiers.push(boardMlmUserIdentifier);
        return true;
      });
      return [...boardMlmUsersToAdd, ...boardMlmUserCollection];
    }
    return boardMlmUserCollection;
  }
}
