import { IUser } from 'app/entities/user/user.model';

export interface IMlmUser {
  id?: number;
  userId?: number | null;
  paid?: boolean | null;
  active?: boolean | null;
  refMlmUserId?: number | null;
  userId?: IUser | null;
}

export class MlmUser implements IMlmUser {
  constructor(
    public id?: number,
    public userId?: number | null,
    public paid?: boolean | null,
    public active?: boolean | null,
    public refMlmUserId?: number | null,
    public userId?: IUser | null
  ) {
    this.paid = this.paid ?? false;
    this.active = this.active ?? false;
  }
}

export function getMlmUserIdentifier(mlmUser: IMlmUser): number | undefined {
  return mlmUser.id;
}
