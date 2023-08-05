import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'applicant',
        data: { pageTitle: 'foreignjobsApp.applicant.home.title' },
        loadChildren: () => import('./applicant/applicant.routes'),
      },
      {
        path: 'company',
        data: { pageTitle: 'foreignjobsApp.company.home.title' },
        loadChildren: () => import('./company/company.routes'),
      },
      {
        path: 'job',
        data: { pageTitle: 'foreignjobsApp.job.home.title' },
        loadChildren: () => import('./job/job.routes'),
      },
      {
        path: 'questionaire',
        data: { pageTitle: 'foreignjobsApp.questionaire.home.title' },
        loadChildren: () => import('./questionaire/questionaire.routes'),
      },
      {
        path: 'task',
        data: { pageTitle: 'foreignjobsApp.task.home.title' },
        loadChildren: () => import('./task/task.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
