import { IQuestionaire, NewQuestionaire } from './questionaire.model';

export const sampleWithRequiredData: IQuestionaire = {
  id: 2918,
};

export const sampleWithPartialData: IQuestionaire = {
  id: 22933,
  options: 'off venerated',
  correctOption: 'West Pound East',
};

export const sampleWithFullData: IQuestionaire = {
  id: 17738,
  questTitle: 'Small',
  options: 'drat Plastic',
  correctOption: 'meh Specialist Cargo',
};

export const sampleWithNewData: NewQuestionaire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
