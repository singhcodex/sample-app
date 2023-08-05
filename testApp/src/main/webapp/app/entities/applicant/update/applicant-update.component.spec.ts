import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApplicantFormService } from './applicant-form.service';
import { ApplicantService } from '../service/applicant.service';
import { IApplicant } from '../applicant.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ApplicantUpdateComponent } from './applicant-update.component';

describe('Applicant Management Update Component', () => {
  let comp: ApplicantUpdateComponent;
  let fixture: ComponentFixture<ApplicantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicantFormService: ApplicantFormService;
  let applicantService: ApplicantService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApplicantUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ApplicantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicantFormService = TestBed.inject(ApplicantFormService);
    applicantService = TestBed.inject(ApplicantService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const applicant: IApplicant = { id: 456 };
      const user: IUser = { id: 28976 };
      applicant.user = user;

      const userCollection: IUser[] = [{ id: 23657 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const applicant: IApplicant = { id: 456 };
      const user: IUser = { id: 13211 };
      applicant.user = user;

      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.applicant).toEqual(applicant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicant>>();
      const applicant = { id: 123 };
      jest.spyOn(applicantFormService, 'getApplicant').mockReturnValue(applicant);
      jest.spyOn(applicantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicant }));
      saveSubject.complete();

      // THEN
      expect(applicantFormService.getApplicant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicantService.update).toHaveBeenCalledWith(expect.objectContaining(applicant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicant>>();
      const applicant = { id: 123 };
      jest.spyOn(applicantFormService, 'getApplicant').mockReturnValue({ id: null });
      jest.spyOn(applicantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicant }));
      saveSubject.complete();

      // THEN
      expect(applicantFormService.getApplicant).toHaveBeenCalled();
      expect(applicantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicant>>();
      const applicant = { id: 123 };
      jest.spyOn(applicantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
