import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 16298,
};

export const sampleWithPartialData: ICompany = {
  id: 9056,
  companyName: 'South',
  email: 'Eda_Sporer@gmail.com',
  city: 'Colorado Springs',
  country: 'Portugal',
  industry: 'Taka',
};

export const sampleWithFullData: ICompany = {
  id: 12048,
  companyName: 'mobile',
  description: 'South Principal Sedan',
  contact: 'Movies Strategist synergize',
  email: 'Adam.Spencer63@yahoo.com',
  streetAddress: 'band',
  postalCode: 'Handcrafted turquoise',
  city: 'Nicholausfurt',
  stateProvince: 'Direct meanwhile',
  country: 'Namibia',
  industry: 'Electric Automotive BMX',
  employeesCount: 3413,
};

export const sampleWithNewData: NewCompany = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
