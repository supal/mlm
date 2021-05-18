import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBoardMlmUser, BoardMlmUser } from '../board-mlm-user.model';

import { BoardMlmUserService } from './board-mlm-user.service';

describe('Service Tests', () => {
  describe('BoardMlmUser Service', () => {
    let service: BoardMlmUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IBoardMlmUser;
    let expectedResult: IBoardMlmUser | IBoardMlmUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BoardMlmUserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        boardId: 0,
        mlmUserId: 0,
        level: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BoardMlmUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BoardMlmUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BoardMlmUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            boardId: 1,
            mlmUserId: 1,
            level: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BoardMlmUser', () => {
        const patchObject = Object.assign(
          {
            boardId: 1,
            mlmUserId: 1,
            level: 1,
          },
          new BoardMlmUser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BoardMlmUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            boardId: 1,
            mlmUserId: 1,
            level: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a BoardMlmUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBoardMlmUserToCollectionIfMissing', () => {
        it('should add a BoardMlmUser to an empty array', () => {
          const boardMlmUser: IBoardMlmUser = { id: 123 };
          expectedResult = service.addBoardMlmUserToCollectionIfMissing([], boardMlmUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardMlmUser);
        });

        it('should not add a BoardMlmUser to an array that contains it', () => {
          const boardMlmUser: IBoardMlmUser = { id: 123 };
          const boardMlmUserCollection: IBoardMlmUser[] = [
            {
              ...boardMlmUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addBoardMlmUserToCollectionIfMissing(boardMlmUserCollection, boardMlmUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BoardMlmUser to an array that doesn't contain it", () => {
          const boardMlmUser: IBoardMlmUser = { id: 123 };
          const boardMlmUserCollection: IBoardMlmUser[] = [{ id: 456 }];
          expectedResult = service.addBoardMlmUserToCollectionIfMissing(boardMlmUserCollection, boardMlmUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardMlmUser);
        });

        it('should add only unique BoardMlmUser to an array', () => {
          const boardMlmUserArray: IBoardMlmUser[] = [{ id: 123 }, { id: 456 }, { id: 51762 }];
          const boardMlmUserCollection: IBoardMlmUser[] = [{ id: 123 }];
          expectedResult = service.addBoardMlmUserToCollectionIfMissing(boardMlmUserCollection, ...boardMlmUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const boardMlmUser: IBoardMlmUser = { id: 123 };
          const boardMlmUser2: IBoardMlmUser = { id: 456 };
          expectedResult = service.addBoardMlmUserToCollectionIfMissing([], boardMlmUser, boardMlmUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardMlmUser);
          expect(expectedResult).toContain(boardMlmUser2);
        });

        it('should accept null and undefined values', () => {
          const boardMlmUser: IBoardMlmUser = { id: 123 };
          expectedResult = service.addBoardMlmUserToCollectionIfMissing([], null, boardMlmUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardMlmUser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
