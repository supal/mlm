import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MlmUserDetailComponent } from './mlm-user-detail.component';

describe('Component Tests', () => {
  describe('MlmUser Management Detail Component', () => {
    let comp: MlmUserDetailComponent;
    let fixture: ComponentFixture<MlmUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MlmUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mlmUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MlmUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MlmUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mlmUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mlmUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
