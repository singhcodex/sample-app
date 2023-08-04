import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionaireComponent } from './list/questionaire.component';
import { QuestionaireDetailComponent } from './detail/questionaire-detail.component';
import { QuestionaireUpdateComponent } from './update/questionaire-update.component';
import QuestionaireResolve from './route/questionaire-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const questionaireRoute: Routes = [
  {
    path: '',
    component: QuestionaireComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionaireDetailComponent,
    resolve: {
      questionaire: QuestionaireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionaireUpdateComponent,
    resolve: {
      questionaire: QuestionaireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionaireUpdateComponent,
    resolve: {
      questionaire: QuestionaireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default questionaireRoute;
