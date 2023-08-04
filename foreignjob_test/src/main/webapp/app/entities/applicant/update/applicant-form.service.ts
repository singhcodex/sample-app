import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IApplicant, NewApplicant } from '../applicant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApplicant for edit and NewApplicantFormGroupInput for create.
 */
type ApplicantFormGroupInput = IApplicant | PartialWithRequiredKeyOf<NewApplicant>;

type ApplicantFormDefaults = Pick<NewApplicant, 'id' | 'jobs'>;

type ApplicantFormGroupContent = {
  id: FormControl<IApplicant['id'] | NewApplicant['id']>;
  firstName: FormControl<IApplicant['firstName']>;
  lastName: FormControl<IApplicant['lastName']>;
  email: FormControl<IApplicant['email']>;
  phoneNumber: FormControl<IApplicant['phoneNumber']>;
  streetAddress: FormControl<IApplicant['streetAddress']>;
  postalCode: FormControl<IApplicant['postalCode']>;
  city: FormControl<IApplicant['city']>;
  stateProvince: FormControl<IApplicant['stateProvince']>;
  country: FormControl<IApplicant['country']>;
  education: FormControl<IApplicant['education']>;
  skills: FormControl<IApplicant['skills']>;
  resume: FormControl<IApplicant['resume']>;
  resumeContentType: FormControl<IApplicant['resumeContentType']>;
  user: FormControl<IApplicant['user']>;
  jobs: FormControl<IApplicant['jobs']>;
};

export type ApplicantFormGroup = FormGroup<ApplicantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApplicantFormService {
  createApplicantFormGroup(applicant: ApplicantFormGroupInput = { id: null }): ApplicantFormGroup {
    const applicantRawValue = {
      ...this.getFormDefaults(),
      ...applicant,
    };
    return new FormGroup<ApplicantFormGroupContent>({
      id: new FormControl(
        { value: applicantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(applicantRawValue.firstName),
      lastName: new FormControl(applicantRawValue.lastName),
      email: new FormControl(applicantRawValue.email),
      phoneNumber: new FormControl(applicantRawValue.phoneNumber),
      streetAddress: new FormControl(applicantRawValue.streetAddress),
      postalCode: new FormControl(applicantRawValue.postalCode),
      city: new FormControl(applicantRawValue.city),
      stateProvince: new FormControl(applicantRawValue.stateProvince),
      country: new FormControl(applicantRawValue.country),
      education: new FormControl(applicantRawValue.education),
      skills: new FormControl(applicantRawValue.skills),
      resume: new FormControl(applicantRawValue.resume),
      resumeContentType: new FormControl(applicantRawValue.resumeContentType),
      user: new FormControl(applicantRawValue.user),
      jobs: new FormControl(applicantRawValue.jobs ?? []),
    });
  }

  getApplicant(form: ApplicantFormGroup): IApplicant | NewApplicant {
    return form.getRawValue() as IApplicant | NewApplicant;
  }

  resetForm(form: ApplicantFormGroup, applicant: ApplicantFormGroupInput): void {
    const applicantRawValue = { ...this.getFormDefaults(), ...applicant };
    form.reset(
      {
        ...applicantRawValue,
        id: { value: applicantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ApplicantFormDefaults {
    return {
      id: null,
      jobs: [],
    };
  }
}
