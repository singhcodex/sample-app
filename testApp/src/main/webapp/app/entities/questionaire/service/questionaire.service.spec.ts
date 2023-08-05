import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionaire } from '../questionaire.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../questionaire.test-samples';

import { QuestionaireService } from './questionaire.service';

const requireRestSample: IQuestionaire = {
  ...sampleWithRequiredData,
};

describe('Questionaire Service', () => {
  let service: QuestionaireService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuestionaire | IQuestionaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionaireService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Questionaire', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const questionaire = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(questionaire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Questionaire', () => {
      const questionaire = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(questionaire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Questionaire', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Questionaire', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Questionaire', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuestionaireToCollectionIfMissing', () => {
      it('should add a Questionaire to an empty array', () => {
        const questionaire: IQuestionaire = sampleWithRequiredData;
        expectedResult = service.addQuestionaireToCollectionIfMissing([], questionaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionaire);
      });

      it('should not add a Questionaire to an array that contains it', () => {
        const questionaire: IQuestionaire = sampleWithRequiredData;
        const questionaireCollection: IQuestionaire[] = [
          {
            ...questionaire,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuestionaireToCollectionIfMissing(questionaireCollection, questionaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Questionaire to an array that doesn't contain it", () => {
        const questionaire: IQuestionaire = sampleWithRequiredData;
        const questionaireCollection: IQuestionaire[] = [sampleWithPartialData];
        expectedResult = service.addQuestionaireToCollectionIfMissing(questionaireCollection, questionaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionaire);
      });

      it('should add only unique Questionaire to an array', () => {
        const questionaireArray: IQuestionaire[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const questionaireCollection: IQuestionaire[] = [sampleWithRequiredData];
        expectedResult = service.addQuestionaireToCollectionIfMissing(questionaireCollection, ...questionaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionaire: IQuestionaire = sampleWithRequiredData;
        const questionaire2: IQuestionaire = sampleWithPartialData;
        expectedResult = service.addQuestionaireToCollectionIfMissing([], questionaire, questionaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionaire);
        expect(expectedResult).toContain(questionaire2);
      });

      it('should accept null and undefined values', () => {
        const questionaire: IQuestionaire = sampleWithRequiredData;
        expectedResult = service.addQuestionaireToCollectionIfMissing([], null, questionaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionaire);
      });

      it('should return initial array if no Questionaire is added', () => {
        const questionaireCollection: IQuestionaire[] = [sampleWithRequiredData];
        expectedResult = service.addQuestionaireToCollectionIfMissing(questionaireCollection, undefined, null);
        expect(expectedResult).toEqual(questionaireCollection);
      });
    });

    describe('compareQuestionaire', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuestionaire(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuestionaire(entity1, entity2);
        const compareResult2 = service.compareQuestionaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuestionaire(entity1, entity2);
        const compareResult2 = service.compareQuestionaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuestionaire(entity1, entity2);
        const compareResult2 = service.compareQuestionaire(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
