import { IUser } from 'app/entities/user/user.model';
import { IJob } from 'app/entities/job/job.model';

export interface IApplicant {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  country?: string | null;
  education?: string | null;
  skills?: string | null;
  resume?: string | null;
  resumeContentType?: string | null;
  user?: Pick<IUser, 'id'> | null;
  jobs?: Pick<IJob, 'id' | 'jobTitle'>[] | null;
}

export type NewApplicant = Omit<IApplicant, 'id'> & { id: null };
