import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../applicant.test-samples';

import { ApplicantFormService } from './applicant-form.service';

describe('Applicant Form Service', () => {
  let service: ApplicantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicantFormService);
  });

  describe('Service methods', () => {
    describe('createApplicantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApplicantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            streetAddress: expect.any(Object),
            postalCode: expect.any(Object),
            city: expect.any(Object),
            stateProvince: expect.any(Object),
            country: expect.any(Object),
            education: expect.any(Object),
            skills: expect.any(Object),
            resume: expect.any(Object),
            user: expect.any(Object),
            jobs: expect.any(Object),
          })
        );
      });

      it('passing IApplicant should create a new form with FormGroup', () => {
        const formGroup = service.createApplicantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            streetAddress: expect.any(Object),
            postalCode: expect.any(Object),
            city: expect.any(Object),
            stateProvince: expect.any(Object),
            country: expect.any(Object),
            education: expect.any(Object),
            skills: expect.any(Object),
            resume: expect.any(Object),
            user: expect.any(Object),
            jobs: expect.any(Object),
          })
        );
      });
    });

    describe('getApplicant', () => {
      it('should return NewApplicant for default Applicant initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createApplicantFormGroup(sampleWithNewData);

        const applicant = service.getApplicant(formGroup) as any;

        expect(applicant).toMatchObject(sampleWithNewData);
      });

      it('should return NewApplicant for empty Applicant initial value', () => {
        const formGroup = service.createApplicantFormGroup();

        const applicant = service.getApplicant(formGroup) as any;

        expect(applicant).toMatchObject({});
      });

      it('should return IApplicant', () => {
        const formGroup = service.createApplicantFormGroup(sampleWithRequiredData);

        const applicant = service.getApplicant(formGroup) as any;

        expect(applicant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApplicant should not enable id FormControl', () => {
        const formGroup = service.createApplicantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApplicant should disable id FormControl', () => {
        const formGroup = service.createApplicantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
