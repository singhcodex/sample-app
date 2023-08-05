import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../questionaire.test-samples';

import { QuestionaireFormService } from './questionaire-form.service';

describe('Questionaire Form Service', () => {
  let service: QuestionaireFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionaireFormService);
  });

  describe('Service methods', () => {
    describe('createQuestionaireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuestionaireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            questTitle: expect.any(Object),
            options: expect.any(Object),
            correctOption: expect.any(Object),
            task: expect.any(Object),
          })
        );
      });

      it('passing IQuestionaire should create a new form with FormGroup', () => {
        const formGroup = service.createQuestionaireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            questTitle: expect.any(Object),
            options: expect.any(Object),
            correctOption: expect.any(Object),
            task: expect.any(Object),
          })
        );
      });
    });

    describe('getQuestionaire', () => {
      it('should return NewQuestionaire for default Questionaire initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createQuestionaireFormGroup(sampleWithNewData);

        const questionaire = service.getQuestionaire(formGroup) as any;

        expect(questionaire).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuestionaire for empty Questionaire initial value', () => {
        const formGroup = service.createQuestionaireFormGroup();

        const questionaire = service.getQuestionaire(formGroup) as any;

        expect(questionaire).toMatchObject({});
      });

      it('should return IQuestionaire', () => {
        const formGroup = service.createQuestionaireFormGroup(sampleWithRequiredData);

        const questionaire = service.getQuestionaire(formGroup) as any;

        expect(questionaire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuestionaire should not enable id FormControl', () => {
        const formGroup = service.createQuestionaireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuestionaire should disable id FormControl', () => {
        const formGroup = service.createQuestionaireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
