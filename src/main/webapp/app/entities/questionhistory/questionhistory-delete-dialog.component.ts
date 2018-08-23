import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQuestionhistory } from 'app/shared/model/questionhistory.model';
import { QuestionhistoryService } from './questionhistory.service';

@Component({
    selector: 'jhi-questionhistory-delete-dialog',
    templateUrl: './questionhistory-delete-dialog.component.html'
})
export class QuestionhistoryDeleteDialogComponent {
    questionhistory: IQuestionhistory;

    constructor(
        private questionhistoryService: QuestionhistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.questionhistoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'questionhistoryListModification',
                content: 'Deleted an questionhistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-questionhistory-delete-popup',
    template: ''
})
export class QuestionhistoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ questionhistory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(QuestionhistoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.questionhistory = questionhistory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
