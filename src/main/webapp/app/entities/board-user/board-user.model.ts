export interface IBoardUser {
  id?: number;
  boardId?: number | null;
  userId?: number | null;
  level?: number | null;
  status?: string | null;
}

export class BoardUser implements IBoardUser {
  constructor(
    public id?: number,
    public boardId?: number | null,
    public userId?: number | null,
    public level?: number | null,
    public status?: string | null
  ) {}
}

export function getBoardUserIdentifier(boardUser: IBoardUser): number | undefined {
  return boardUser.id;
}
