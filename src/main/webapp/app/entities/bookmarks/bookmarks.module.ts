import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartCpdSharedModule } from 'app/shared';
import {
    BookmarksComponent,
    BookmarksDetailComponent,
    BookmarksUpdateComponent,
    BookmarksDeletePopupComponent,
    BookmarksDeleteDialogComponent,
    bookmarksRoute,
    bookmarksPopupRoute
} from './';

const ENTITY_STATES = [...bookmarksRoute, ...bookmarksPopupRoute];

@NgModule({
    imports: [SmartCpdSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BookmarksComponent,
        BookmarksDetailComponent,
        BookmarksUpdateComponent,
        BookmarksDeleteDialogComponent,
        BookmarksDeletePopupComponent
    ],
    entryComponents: [BookmarksComponent, BookmarksUpdateComponent, BookmarksDeleteDialogComponent, BookmarksDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdBookmarksModule {}
