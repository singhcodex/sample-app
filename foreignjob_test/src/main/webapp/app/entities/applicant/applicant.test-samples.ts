import { IApplicant, NewApplicant } from './applicant.model';

export const sampleWithRequiredData: IApplicant = {
  id: 27863,
};

export const sampleWithPartialData: IApplicant = {
  id: 10097,
  firstName: 'Jalen',
  postalCode: 'Intranet hungrily',
  city: 'Port Camilla',
  education: 'drive',
  skills: 'Northeast Minnesota',
};

export const sampleWithFullData: IApplicant = {
  id: 17681,
  firstName: 'Grayson',
  lastName: 'Terry',
  email: 'Connie_Gusikowski@gmail.com',
  phoneNumber: 'neural Cambridgeshire',
  streetAddress: 'deposit Internal',
  postalCode: 'Pop honestly famously',
  city: 'Bauchcester',
  stateProvince: 'Diesel East',
  country: 'Virgin Islands, British',
  education: 'turquoise',
  skills: 'invoice Intelligent',
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
};

export const sampleWithNewData: NewApplicant = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
