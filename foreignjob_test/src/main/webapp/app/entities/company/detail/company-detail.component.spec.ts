import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CompanyDetailComponent } from './company-detail.component';

describe('Company Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompanyDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CompanyDetailComponent,
              resolve: { company: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CompanyDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load company on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CompanyDetailComponent);

      // THEN
      expect(instance.company).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
