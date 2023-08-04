import dayjs from 'dayjs/esm';

import { Language } from 'app/entities/enumerations/language.model';

import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: 32212,
};

export const sampleWithPartialData: IJob = {
  id: 11587,
  jobTitle: 'Dynamic Program Specialist',
  department: 'metrics Antimony',
  industry: 'port application Officer',
  expiryDate: dayjs('2023-08-03'),
  streetAddress: 'excitedly Central',
  postalCode: 'Southeast generating East',
  city: 'Adonisstead',
  stateProvince: 'digital bypassing niches',
  country: 'Pitcairn Islands',
  jobResponsibility: 'nonbeliever',
  skills: 'Transexual calculate gust',
  language: 'FRENCH',
};

export const sampleWithFullData: IJob = {
  id: 20821,
  jobTitle: 'Direct Assurance Specialist',
  department: 'Incredible Markets',
  industry: 'Mayotte Account from',
  vacancies: 1211,
  expiryDate: dayjs('2023-08-04'),
  streetAddress: 'withdrawal',
  postalCode: 'Ameliorated deliver',
  city: 'North Kevon',
  stateProvince: 'blue',
  country: 'French Guiana',
  jobRequirement: 'Loan Curacao',
  jobResponsibility: 'synthesizing',
  skills: 'Luxurious',
  language: 'DEUTSCH',
  minSalary: 31620,
  maxSalary: 24513,
  workingHours: 23957,
  benefits: 'than Northeast transmit',
};

export const sampleWithNewData: NewJob = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
