jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBoardUser, BoardUser } from '../board-user.model';
import { BoardUserService } from '../service/board-user.service';

import { BoardUserRoutingResolveService } from './board-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('BoardUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BoardUserRoutingResolveService;
    let service: BoardUserService;
    let resultBoardUser: IBoardUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BoardUserRoutingResolveService);
      service = TestBed.inject(BoardUserService);
      resultBoardUser = undefined;
    });

    describe('resolve', () => {
      it('should return IBoardUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoardUser).toEqual({ id: 123 });
      });

      it('should return new IBoardUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBoardUser).toEqual(new BoardUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoardUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
