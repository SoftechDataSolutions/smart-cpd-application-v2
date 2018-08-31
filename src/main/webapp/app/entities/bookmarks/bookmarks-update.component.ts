import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBookmarks } from 'app/shared/model/bookmarks.model';
import { BookmarksService } from './bookmarks.service';
import { ISection } from 'app/shared/model/section.model';
import { SectionService } from 'app/entities/section';

@Component({
    selector: 'jhi-bookmarks-update',
    templateUrl: './bookmarks-update.component.html'
})
export class BookmarksUpdateComponent implements OnInit {
    private _bookmarks: IBookmarks;
    isSaving: boolean;

    sections: ISection[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private bookmarksService: BookmarksService,
        private sectionService: SectionService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ bookmarks }) => {
            this.bookmarks = bookmarks;
        });
        this.sectionService.query().subscribe(
            (res: HttpResponse<ISection[]>) => {
                this.sections = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.bookmarks.id !== undefined) {
            this.subscribeToSaveResponse(this.bookmarksService.update(this.bookmarks));
        } else {
            this.subscribeToSaveResponse(this.bookmarksService.create(this.bookmarks));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBookmarks>>) {
        result.subscribe((res: HttpResponse<IBookmarks>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackSectionById(index: number, item: ISection) {
        return item.id;
    }
    get bookmarks() {
        return this._bookmarks;
    }

    set bookmarks(bookmarks: IBookmarks) {
        this._bookmarks = bookmarks;
    }
}
