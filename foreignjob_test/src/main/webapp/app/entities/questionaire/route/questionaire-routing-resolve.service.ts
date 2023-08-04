import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionaire } from '../questionaire.model';
import { QuestionaireService } from '../service/questionaire.service';

export const questionaireResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuestionaire> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuestionaireService)
      .find(id)
      .pipe(
        mergeMap((questionaire: HttpResponse<IQuestionaire>) => {
          if (questionaire.body) {
            return of(questionaire.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default questionaireResolve;
