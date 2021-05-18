import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoardMlmUserDetailComponent } from './board-mlm-user-detail.component';

describe('Component Tests', () => {
  describe('BoardMlmUser Management Detail Component', () => {
    let comp: BoardMlmUserDetailComponent;
    let fixture: ComponentFixture<BoardMlmUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BoardMlmUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ boardMlmUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BoardMlmUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoardMlmUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load boardMlmUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.boardMlmUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
