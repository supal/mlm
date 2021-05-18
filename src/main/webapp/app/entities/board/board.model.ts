import { BoardType } from 'app/entities/enumerations/board-type.model';

export interface IBoard {
  id?: number;
  type?: BoardType;
}

export class Board implements IBoard {
  constructor(public id?: number, public type?: BoardType) {}
}

export function getBoardIdentifier(board: IBoard): number | undefined {
  return board.id;
}
