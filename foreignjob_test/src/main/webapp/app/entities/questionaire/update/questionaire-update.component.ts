import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { QuestionaireFormService, QuestionaireFormGroup } from './questionaire-form.service';
import { IQuestionaire } from '../questionaire.model';
import { QuestionaireService } from '../service/questionaire.service';
import { ITask } from 'app/entities/task/task.model';
import { TaskService } from 'app/entities/task/service/task.service';

@Component({
  standalone: true,
  selector: 'jhi-questionaire-update',
  templateUrl: './questionaire-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuestionaireUpdateComponent implements OnInit {
  isSaving = false;
  questionaire: IQuestionaire | null = null;

  tasksSharedCollection: ITask[] = [];

  editForm: QuestionaireFormGroup = this.questionaireFormService.createQuestionaireFormGroup();

  constructor(
    protected questionaireService: QuestionaireService,
    protected questionaireFormService: QuestionaireFormService,
    protected taskService: TaskService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTask = (o1: ITask | null, o2: ITask | null): boolean => this.taskService.compareTask(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionaire }) => {
      this.questionaire = questionaire;
      if (questionaire) {
        this.updateForm(questionaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionaire = this.questionaireFormService.getQuestionaire(this.editForm);
    if (questionaire.id !== null) {
      this.subscribeToSaveResponse(this.questionaireService.update(questionaire));
    } else {
      this.subscribeToSaveResponse(this.questionaireService.create(questionaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionaire>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(questionaire: IQuestionaire): void {
    this.questionaire = questionaire;
    this.questionaireFormService.resetForm(this.editForm, questionaire);

    this.tasksSharedCollection = this.taskService.addTaskToCollectionIfMissing<ITask>(this.tasksSharedCollection, questionaire.task);
  }

  protected loadRelationshipsOptions(): void {
    this.taskService
      .query()
      .pipe(map((res: HttpResponse<ITask[]>) => res.body ?? []))
      .pipe(map((tasks: ITask[]) => this.taskService.addTaskToCollectionIfMissing<ITask>(tasks, this.questionaire?.task)))
      .subscribe((tasks: ITask[]) => (this.tasksSharedCollection = tasks));
  }
}
