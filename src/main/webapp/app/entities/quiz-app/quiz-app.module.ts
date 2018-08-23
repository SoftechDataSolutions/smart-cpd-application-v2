import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartCpdSharedModule } from 'app/shared';
import {
    QuizAppComponent,
    QuizAppDetailComponent,
    QuizAppUpdateComponent,
    QuizAppDeletePopupComponent,
    QuizAppDeleteDialogComponent,
    quizAppRoute,
    quizAppPopupRoute
} from './';

const ENTITY_STATES = [...quizAppRoute, ...quizAppPopupRoute];

@NgModule({
    imports: [SmartCpdSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        QuizAppComponent,
        QuizAppDetailComponent,
        QuizAppUpdateComponent,
        QuizAppDeleteDialogComponent,
        QuizAppDeletePopupComponent
    ],
    entryComponents: [QuizAppComponent, QuizAppUpdateComponent, QuizAppDeleteDialogComponent, QuizAppDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdQuizAppModule {}
