import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartCpdSharedModule } from 'app/shared';
import {
    QuestionhistoryComponent,
    QuestionhistoryDetailComponent,
    QuestionhistoryUpdateComponent,
    QuestionhistoryDeletePopupComponent,
    QuestionhistoryDeleteDialogComponent,
    questionhistoryRoute,
    questionhistoryPopupRoute
} from './';

const ENTITY_STATES = [...questionhistoryRoute, ...questionhistoryPopupRoute];

@NgModule({
    imports: [SmartCpdSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        QuestionhistoryComponent,
        QuestionhistoryDetailComponent,
        QuestionhistoryUpdateComponent,
        QuestionhistoryDeleteDialogComponent,
        QuestionhistoryDeletePopupComponent
    ],
    entryComponents: [
        QuestionhistoryComponent,
        QuestionhistoryUpdateComponent,
        QuestionhistoryDeleteDialogComponent,
        QuestionhistoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdQuestionhistoryModule {}
