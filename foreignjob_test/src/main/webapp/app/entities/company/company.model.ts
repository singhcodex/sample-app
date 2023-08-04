import { IUser } from 'app/entities/user/user.model';

export interface ICompany {
  id: number;
  companyName?: string | null;
  description?: string | null;
  contact?: string | null;
  email?: string | null;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  country?: string | null;
  industry?: string | null;
  employeesCount?: number | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
