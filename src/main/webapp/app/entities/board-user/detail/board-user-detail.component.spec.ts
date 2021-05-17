import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoardUserDetailComponent } from './board-user-detail.component';

describe('Component Tests', () => {
  describe('BoardUser Management Detail Component', () => {
    let comp: BoardUserDetailComponent;
    let fixture: ComponentFixture<BoardUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BoardUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ boardUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BoardUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoardUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load boardUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.boardUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
