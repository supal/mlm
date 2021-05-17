import { BoardType } from 'app/entities/enumerations/board-type.model';

export interface IBoard {
  boardId?: number;
  type?: BoardType;
  status?: string | null;
}

export class Board implements IBoard {
  constructor(public boardId?: number, public type?: BoardType, public status?: string | null) {}
}

export function getBoardIdentifier(board: IBoard): number | undefined {
  return board.boardId;
}
