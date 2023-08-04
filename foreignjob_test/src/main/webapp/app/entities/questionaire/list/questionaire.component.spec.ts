import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuestionaireService } from '../service/questionaire.service';

import { QuestionaireComponent } from './questionaire.component';

describe('Questionaire Management Component', () => {
  let comp: QuestionaireComponent;
  let fixture: ComponentFixture<QuestionaireComponent>;
  let service: QuestionaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'questionaire', component: QuestionaireComponent }]),
        HttpClientTestingModule,
        QuestionaireComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(QuestionaireComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionaireComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuestionaireService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.questionaires?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to questionaireService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getQuestionaireIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getQuestionaireIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
