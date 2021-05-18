import { IBoard } from 'app/entities/board/board.model';
import { IMlmUser } from 'app/entities/mlm-user/mlm-user.model';

export interface IBoardMlmUser {
  id?: number;
  boardId?: number | null;
  mlmUserId?: number | null;
  level?: number | null;
  boardId?: IBoard | null;
  mlmUserId?: IMlmUser | null;
}

export class BoardMlmUser implements IBoardMlmUser {
  constructor(
    public id?: number,
    public boardId?: number | null,
    public mlmUserId?: number | null,
    public level?: number | null,
    public boardId?: IBoard | null,
    public mlmUserId?: IMlmUser | null
  ) {}
}

export function getBoardMlmUserIdentifier(boardMlmUser: IBoardMlmUser): number | undefined {
  return boardMlmUser.id;
}
