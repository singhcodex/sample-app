import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IJob, NewJob } from '../job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJob for edit and NewJobFormGroupInput for create.
 */
type JobFormGroupInput = IJob | PartialWithRequiredKeyOf<NewJob>;

type JobFormDefaults = Pick<NewJob, 'id' | 'tasks' | 'applicants'>;

type JobFormGroupContent = {
  id: FormControl<IJob['id'] | NewJob['id']>;
  jobTitle: FormControl<IJob['jobTitle']>;
  department: FormControl<IJob['department']>;
  industry: FormControl<IJob['industry']>;
  vacancies: FormControl<IJob['vacancies']>;
  expiryDate: FormControl<IJob['expiryDate']>;
  streetAddress: FormControl<IJob['streetAddress']>;
  postalCode: FormControl<IJob['postalCode']>;
  city: FormControl<IJob['city']>;
  stateProvince: FormControl<IJob['stateProvince']>;
  country: FormControl<IJob['country']>;
  jobRequirement: FormControl<IJob['jobRequirement']>;
  jobResponsibility: FormControl<IJob['jobResponsibility']>;
  skills: FormControl<IJob['skills']>;
  language: FormControl<IJob['language']>;
  minSalary: FormControl<IJob['minSalary']>;
  maxSalary: FormControl<IJob['maxSalary']>;
  workingHours: FormControl<IJob['workingHours']>;
  benefits: FormControl<IJob['benefits']>;
  tasks: FormControl<IJob['tasks']>;
  applicants: FormControl<IJob['applicants']>;
  company: FormControl<IJob['company']>;
};

export type JobFormGroup = FormGroup<JobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JobFormService {
  createJobFormGroup(job: JobFormGroupInput = { id: null }): JobFormGroup {
    const jobRawValue = {
      ...this.getFormDefaults(),
      ...job,
    };
    return new FormGroup<JobFormGroupContent>({
      id: new FormControl(
        { value: jobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      jobTitle: new FormControl(jobRawValue.jobTitle),
      department: new FormControl(jobRawValue.department),
      industry: new FormControl(jobRawValue.industry),
      vacancies: new FormControl(jobRawValue.vacancies),
      expiryDate: new FormControl(jobRawValue.expiryDate),
      streetAddress: new FormControl(jobRawValue.streetAddress),
      postalCode: new FormControl(jobRawValue.postalCode),
      city: new FormControl(jobRawValue.city),
      stateProvince: new FormControl(jobRawValue.stateProvince),
      country: new FormControl(jobRawValue.country),
      jobRequirement: new FormControl(jobRawValue.jobRequirement),
      jobResponsibility: new FormControl(jobRawValue.jobResponsibility),
      skills: new FormControl(jobRawValue.skills),
      language: new FormControl(jobRawValue.language),
      minSalary: new FormControl(jobRawValue.minSalary),
      maxSalary: new FormControl(jobRawValue.maxSalary),
      workingHours: new FormControl(jobRawValue.workingHours),
      benefits: new FormControl(jobRawValue.benefits),
      tasks: new FormControl(jobRawValue.tasks ?? []),
      applicants: new FormControl(jobRawValue.applicants ?? []),
      company: new FormControl(jobRawValue.company),
    });
  }

  getJob(form: JobFormGroup): IJob | NewJob {
    return form.getRawValue() as IJob | NewJob;
  }

  resetForm(form: JobFormGroup, job: JobFormGroupInput): void {
    const jobRawValue = { ...this.getFormDefaults(), ...job };
    form.reset(
      {
        ...jobRawValue,
        id: { value: jobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): JobFormDefaults {
    return {
      id: null,
      tasks: [],
      applicants: [],
    };
  }
}
