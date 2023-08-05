import { IJob } from 'app/entities/job/job.model';

export interface ITask {
  id: number;
  title?: string | null;
  description?: string | null;
  duration?: number | null;
  marks?: number | null;
  jobs?: Pick<IJob, 'id'>[] | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
