import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBookmarks } from 'app/shared/model/bookmarks.model';

@Component({
    selector: 'jhi-bookmarks-detail',
    templateUrl: './bookmarks-detail.component.html'
})
export class BookmarksDetailComponent implements OnInit {
    bookmarks: IBookmarks;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookmarks }) => {
            this.bookmarks = bookmarks;
        });
    }

    previousState() {
        window.history.back();
    }
}
