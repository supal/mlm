import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoardUser, getBoardUserIdentifier } from '../board-user.model';

export type EntityResponseType = HttpResponse<IBoardUser>;
export type EntityArrayResponseType = HttpResponse<IBoardUser[]>;

@Injectable({ providedIn: 'root' })
export class BoardUserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/board-users');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(boardUser: IBoardUser): Observable<EntityResponseType> {
    return this.http.post<IBoardUser>(this.resourceUrl, boardUser, { observe: 'response' });
  }

  update(boardUser: IBoardUser): Observable<EntityResponseType> {
    return this.http.put<IBoardUser>(`${this.resourceUrl}/${getBoardUserIdentifier(boardUser) as number}`, boardUser, {
      observe: 'response',
    });
  }

  partialUpdate(boardUser: IBoardUser): Observable<EntityResponseType> {
    return this.http.patch<IBoardUser>(`${this.resourceUrl}/${getBoardUserIdentifier(boardUser) as number}`, boardUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoardUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoardUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBoardUserToCollectionIfMissing(
    boardUserCollection: IBoardUser[],
    ...boardUsersToCheck: (IBoardUser | null | undefined)[]
  ): IBoardUser[] {
    const boardUsers: IBoardUser[] = boardUsersToCheck.filter(isPresent);
    if (boardUsers.length > 0) {
      const boardUserCollectionIdentifiers = boardUserCollection.map(boardUserItem => getBoardUserIdentifier(boardUserItem)!);
      const boardUsersToAdd = boardUsers.filter(boardUserItem => {
        const boardUserIdentifier = getBoardUserIdentifier(boardUserItem);
        if (boardUserIdentifier == null || boardUserCollectionIdentifiers.includes(boardUserIdentifier)) {
          return false;
        }
        boardUserCollectionIdentifiers.push(boardUserIdentifier);
        return true;
      });
      return [...boardUsersToAdd, ...boardUserCollection];
    }
    return boardUserCollection;
  }
}
