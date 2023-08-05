import dayjs from 'dayjs/esm';
import { ITask } from 'app/entities/task/task.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ICompany } from 'app/entities/company/company.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IJob {
  id: number;
  jobTitle?: string | null;
  department?: string | null;
  industry?: string | null;
  vacancies?: number | null;
  expiryDate?: dayjs.Dayjs | null;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  country?: string | null;
  jobRequirement?: string | null;
  jobResponsibility?: string | null;
  skills?: string | null;
  language?: keyof typeof Language | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  workingHours?: number | null;
  benefits?: string | null;
  tasks?: Pick<ITask, 'id' | 'title'>[] | null;
  applicants?: Pick<IApplicant, 'id' | 'email'>[] | null;
  company?: Pick<ICompany, 'id'> | null;
}

export type NewJob = Omit<IJob, 'id'> & { id: null };
