import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMlmUser, MlmUser } from '../mlm-user.model';

import { MlmUserService } from './mlm-user.service';

describe('Service Tests', () => {
  describe('MlmUser Service', () => {
    let service: MlmUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IMlmUser;
    let expectedResult: IMlmUser | IMlmUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MlmUserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        userId: 0,
        paid: false,
        active: false,
        refMlmUserId: 0,
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

      it('should create a MlmUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MlmUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MlmUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            userId: 1,
            paid: true,
            active: true,
            refMlmUserId: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MlmUser', () => {
        const patchObject = Object.assign(
          {
            userId: 1,
            active: true,
            refMlmUserId: 1,
          },
          new MlmUser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MlmUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            userId: 1,
            paid: true,
            active: true,
            refMlmUserId: 1,
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

      it('should delete a MlmUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMlmUserToCollectionIfMissing', () => {
        it('should add a MlmUser to an empty array', () => {
          const mlmUser: IMlmUser = { id: 123 };
          expectedResult = service.addMlmUserToCollectionIfMissing([], mlmUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mlmUser);
        });

        it('should not add a MlmUser to an array that contains it', () => {
          const mlmUser: IMlmUser = { id: 123 };
          const mlmUserCollection: IMlmUser[] = [
            {
              ...mlmUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addMlmUserToCollectionIfMissing(mlmUserCollection, mlmUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MlmUser to an array that doesn't contain it", () => {
          const mlmUser: IMlmUser = { id: 123 };
          const mlmUserCollection: IMlmUser[] = [{ id: 456 }];
          expectedResult = service.addMlmUserToCollectionIfMissing(mlmUserCollection, mlmUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mlmUser);
        });

        it('should add only unique MlmUser to an array', () => {
          const mlmUserArray: IMlmUser[] = [{ id: 123 }, { id: 456 }, { id: 92328 }];
          const mlmUserCollection: IMlmUser[] = [{ id: 123 }];
          expectedResult = service.addMlmUserToCollectionIfMissing(mlmUserCollection, ...mlmUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mlmUser: IMlmUser = { id: 123 };
          const mlmUser2: IMlmUser = { id: 456 };
          expectedResult = service.addMlmUserToCollectionIfMissing([], mlmUser, mlmUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mlmUser);
          expect(expectedResult).toContain(mlmUser2);
        });

        it('should accept null and undefined values', () => {
          const mlmUser: IMlmUser = { id: 123 };
          expectedResult = service.addMlmUserToCollectionIfMissing([], null, mlmUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mlmUser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
