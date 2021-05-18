import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMlmUser, getMlmUserIdentifier } from '../mlm-user.model';

export type EntityResponseType = HttpResponse<IMlmUser>;
export type EntityArrayResponseType = HttpResponse<IMlmUser[]>;

@Injectable({ providedIn: 'root' })
export class MlmUserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/mlm-users');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(mlmUser: IMlmUser): Observable<EntityResponseType> {
    return this.http.post<IMlmUser>(this.resourceUrl, mlmUser, { observe: 'response' });
  }

  update(mlmUser: IMlmUser): Observable<EntityResponseType> {
    return this.http.put<IMlmUser>(`${this.resourceUrl}/${getMlmUserIdentifier(mlmUser) as number}`, mlmUser, { observe: 'response' });
  }

  partialUpdate(mlmUser: IMlmUser): Observable<EntityResponseType> {
    return this.http.patch<IMlmUser>(`${this.resourceUrl}/${getMlmUserIdentifier(mlmUser) as number}`, mlmUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMlmUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMlmUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMlmUserToCollectionIfMissing(mlmUserCollection: IMlmUser[], ...mlmUsersToCheck: (IMlmUser | null | undefined)[]): IMlmUser[] {
    const mlmUsers: IMlmUser[] = mlmUsersToCheck.filter(isPresent);
    if (mlmUsers.length > 0) {
      const mlmUserCollectionIdentifiers = mlmUserCollection.map(mlmUserItem => getMlmUserIdentifier(mlmUserItem)!);
      const mlmUsersToAdd = mlmUsers.filter(mlmUserItem => {
        const mlmUserIdentifier = getMlmUserIdentifier(mlmUserItem);
        if (mlmUserIdentifier == null || mlmUserCollectionIdentifiers.includes(mlmUserIdentifier)) {
          return false;
        }
        mlmUserCollectionIdentifiers.push(mlmUserIdentifier);
        return true;
      });
      return [...mlmUsersToAdd, ...mlmUserCollection];
    }
    return mlmUserCollection;
  }
}
