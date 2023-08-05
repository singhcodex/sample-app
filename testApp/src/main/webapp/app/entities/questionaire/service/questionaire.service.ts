import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionaire, NewQuestionaire } from '../questionaire.model';

export type PartialUpdateQuestionaire = Partial<IQuestionaire> & Pick<IQuestionaire, 'id'>;

export type EntityResponseType = HttpResponse<IQuestionaire>;
export type EntityArrayResponseType = HttpResponse<IQuestionaire[]>;

@Injectable({ providedIn: 'root' })
export class QuestionaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questionaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionaire: NewQuestionaire): Observable<EntityResponseType> {
    return this.http.post<IQuestionaire>(this.resourceUrl, questionaire, { observe: 'response' });
  }

  update(questionaire: IQuestionaire): Observable<EntityResponseType> {
    return this.http.put<IQuestionaire>(`${this.resourceUrl}/${this.getQuestionaireIdentifier(questionaire)}`, questionaire, {
      observe: 'response',
    });
  }

  partialUpdate(questionaire: PartialUpdateQuestionaire): Observable<EntityResponseType> {
    return this.http.patch<IQuestionaire>(`${this.resourceUrl}/${this.getQuestionaireIdentifier(questionaire)}`, questionaire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuestionaireIdentifier(questionaire: Pick<IQuestionaire, 'id'>): number {
    return questionaire.id;
  }

  compareQuestionaire(o1: Pick<IQuestionaire, 'id'> | null, o2: Pick<IQuestionaire, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuestionaireIdentifier(o1) === this.getQuestionaireIdentifier(o2) : o1 === o2;
  }

  addQuestionaireToCollectionIfMissing<Type extends Pick<IQuestionaire, 'id'>>(
    questionaireCollection: Type[],
    ...questionairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const questionaires: Type[] = questionairesToCheck.filter(isPresent);
    if (questionaires.length > 0) {
      const questionaireCollectionIdentifiers = questionaireCollection.map(
        questionaireItem => this.getQuestionaireIdentifier(questionaireItem)!
      );
      const questionairesToAdd = questionaires.filter(questionaireItem => {
        const questionaireIdentifier = this.getQuestionaireIdentifier(questionaireItem);
        if (questionaireCollectionIdentifiers.includes(questionaireIdentifier)) {
          return false;
        }
        questionaireCollectionIdentifiers.push(questionaireIdentifier);
        return true;
      });
      return [...questionairesToAdd, ...questionaireCollection];
    }
    return questionaireCollection;
  }
}
