import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBoardUser, BoardUser } from '../board-user.model';

import { BoardUserService } from './board-user.service';

describe('Service Tests', () => {
  describe('BoardUser Service', () => {
    let service: BoardUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IBoardUser;
    let expectedResult: IBoardUser | IBoardUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BoardUserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        boardId: 0,
        userId: 0,
        level: 0,
        status: 'AAAAAAA',
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

      it('should create a BoardUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BoardUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BoardUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            boardId: 1,
            userId: 1,
            level: 1,
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BoardUser', () => {
        const patchObject = Object.assign(
          {
            userId: 1,
            level: 1,
            status: 'BBBBBB',
          },
          new BoardUser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BoardUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            boardId: 1,
            userId: 1,
            level: 1,
            status: 'BBBBBB',
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

      it('should delete a BoardUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBoardUserToCollectionIfMissing', () => {
        it('should add a BoardUser to an empty array', () => {
          const boardUser: IBoardUser = { id: 123 };
          expectedResult = service.addBoardUserToCollectionIfMissing([], boardUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardUser);
        });

        it('should not add a BoardUser to an array that contains it', () => {
          const boardUser: IBoardUser = { id: 123 };
          const boardUserCollection: IBoardUser[] = [
            {
              ...boardUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addBoardUserToCollectionIfMissing(boardUserCollection, boardUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BoardUser to an array that doesn't contain it", () => {
          const boardUser: IBoardUser = { id: 123 };
          const boardUserCollection: IBoardUser[] = [{ id: 456 }];
          expectedResult = service.addBoardUserToCollectionIfMissing(boardUserCollection, boardUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardUser);
        });

        it('should add only unique BoardUser to an array', () => {
          const boardUserArray: IBoardUser[] = [{ id: 123 }, { id: 456 }, { id: 96165 }];
          const boardUserCollection: IBoardUser[] = [{ id: 123 }];
          expectedResult = service.addBoardUserToCollectionIfMissing(boardUserCollection, ...boardUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const boardUser: IBoardUser = { id: 123 };
          const boardUser2: IBoardUser = { id: 456 };
          expectedResult = service.addBoardUserToCollectionIfMissing([], boardUser, boardUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardUser);
          expect(expectedResult).toContain(boardUser2);
        });

        it('should accept null and undefined values', () => {
          const boardUser: IBoardUser = { id: 123 };
          expectedResult = service.addBoardUserToCollectionIfMissing([], null, boardUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardUser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
