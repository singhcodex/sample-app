import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuestionaireDetailComponent } from './questionaire-detail.component';

describe('Questionaire Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionaireDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QuestionaireDetailComponent,
              resolve: { questionaire: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(QuestionaireDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load questionaire on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuestionaireDetailComponent);

      // THEN
      expect(instance.questionaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
