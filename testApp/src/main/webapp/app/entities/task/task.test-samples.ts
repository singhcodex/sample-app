import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 16882,
};

export const sampleWithPartialData: ITask = {
  id: 32324,
  title: 'North Honda',
  duration: 5959,
  marks: 20103,
};

export const sampleWithFullData: ITask = {
  id: 32416,
  title: 'Granite Tandem Dodge',
  description: 'Coupe',
  duration: 17521,
  marks: 14378,
};

export const sampleWithNewData: NewTask = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
