import { ITask } from 'app/entities/task/task.model';

export interface IQuestionaire {
  id: number;
  questTitle?: string | null;
  options?: string | null;
  correctOption?: string | null;
  task?: Pick<ITask, 'id'> | null;
}

export type NewQuestionaire = Omit<IQuestionaire, 'id'> & { id: null };
