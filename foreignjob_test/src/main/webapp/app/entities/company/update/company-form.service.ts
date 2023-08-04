import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompany, NewCompany } from '../company.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompany for edit and NewCompanyFormGroupInput for create.
 */
type CompanyFormGroupInput = ICompany | PartialWithRequiredKeyOf<NewCompany>;

type CompanyFormDefaults = Pick<NewCompany, 'id'>;

type CompanyFormGroupContent = {
  id: FormControl<ICompany['id'] | NewCompany['id']>;
  companyName: FormControl<ICompany['companyName']>;
  description: FormControl<ICompany['description']>;
  contact: FormControl<ICompany['contact']>;
  email: FormControl<ICompany['email']>;
  streetAddress: FormControl<ICompany['streetAddress']>;
  postalCode: FormControl<ICompany['postalCode']>;
  city: FormControl<ICompany['city']>;
  stateProvince: FormControl<ICompany['stateProvince']>;
  country: FormControl<ICompany['country']>;
  industry: FormControl<ICompany['industry']>;
  employeesCount: FormControl<ICompany['employeesCount']>;
  user: FormControl<ICompany['user']>;
};

export type CompanyFormGroup = FormGroup<CompanyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompanyFormService {
  createCompanyFormGroup(company: CompanyFormGroupInput = { id: null }): CompanyFormGroup {
    const companyRawValue = {
      ...this.getFormDefaults(),
      ...company,
    };
    return new FormGroup<CompanyFormGroupContent>({
      id: new FormControl(
        { value: companyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      companyName: new FormControl(companyRawValue.companyName),
      description: new FormControl(companyRawValue.description),
      contact: new FormControl(companyRawValue.contact),
      email: new FormControl(companyRawValue.email),
      streetAddress: new FormControl(companyRawValue.streetAddress),
      postalCode: new FormControl(companyRawValue.postalCode),
      city: new FormControl(companyRawValue.city),
      stateProvince: new FormControl(companyRawValue.stateProvince),
      country: new FormControl(companyRawValue.country),
      industry: new FormControl(companyRawValue.industry),
      employeesCount: new FormControl(companyRawValue.employeesCount),
      user: new FormControl(companyRawValue.user),
    });
  }

  getCompany(form: CompanyFormGroup): ICompany | NewCompany {
    return form.getRawValue() as ICompany | NewCompany;
  }

  resetForm(form: CompanyFormGroup, company: CompanyFormGroupInput): void {
    const companyRawValue = { ...this.getFormDefaults(), ...company };
    form.reset(
      {
        ...companyRawValue,
        id: { value: companyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompanyFormDefaults {
    return {
      id: null,
    };
  }
}
