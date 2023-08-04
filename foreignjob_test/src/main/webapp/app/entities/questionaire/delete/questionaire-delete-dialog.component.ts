import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IQuestionaire } from '../questionaire.model';
import { QuestionaireService } from '../service/questionaire.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './questionaire-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuestionaireDeleteDialogComponent {
  questionaire?: IQuestionaire;

  constructor(protected questionaireService: QuestionaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionaireService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
