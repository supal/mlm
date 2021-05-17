jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBoard, Board } from '../board.model';
import { BoardService } from '../service/board.service';

import { BoardRoutingResolveService } from './board-routing-resolve.service';

describe('Service Tests', () => {
  describe('Board routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BoardRoutingResolveService;
    let service: BoardService;
    let resultBoard: IBoard | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BoardRoutingResolveService);
      service = TestBed.inject(BoardService);
      resultBoard = undefined;
    });

    describe('resolve', () => {
      it('should return IBoard returned by find', () => {
        // GIVEN
        service.find = jest.fn(boardId => of(new HttpResponse({ body: { boardId } })));
        mockActivatedRouteSnapshot.params = { boardId: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoard = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoard).toEqual({ boardId: 123 });
      });

      it('should return new IBoard if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoard = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBoard).toEqual(new Board());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { boardId: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoard = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoard).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
