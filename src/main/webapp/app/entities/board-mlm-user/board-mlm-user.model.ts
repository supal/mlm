import { IBoard } from 'app/entities/board/board.model';
import { IMlmUser } from 'app/entities/mlm-user/mlm-user.model';

export interface IBoardMlmUser {
  id?: number;
  boardId?: number | null;
  mlmUserId?: number | null;
  level?: number | null;
  board?: IBoard | null;
  mlmUser?: IMlmUser | null;
}

export class BoardMlmUser implements IBoardMlmUser {
  constructor(
    public id?: number,
    public boardId?: number | null,
    public mlmUserId?: number | null,
    public level?: number | null,
    public board?: IBoard | null,
    public mlmUser?: IMlmUser | null
  ) {}
}

export function getBoardMlmUserIdentifier(boardMlmUser: IBoardMlmUser): number | undefined {
  return boardMlmUser.id;
}
