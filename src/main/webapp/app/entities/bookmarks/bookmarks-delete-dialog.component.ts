import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBookmarks } from 'app/shared/model/bookmarks.model';
import { BookmarksService } from './bookmarks.service';

@Component({
    selector: 'jhi-bookmarks-delete-dialog',
    templateUrl: './bookmarks-delete-dialog.component.html'
})
export class BookmarksDeleteDialogComponent {
    bookmarks: IBookmarks;

    constructor(private bookmarksService: BookmarksService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bookmarksService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'bookmarksListModification',
                content: 'Deleted an bookmarks'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bookmarks-delete-popup',
    template: ''
})
export class BookmarksDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookmarks }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BookmarksDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.bookmarks = bookmarks;
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
