import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { QuestionHistory } from 'app/shared/model/questionhistory.model';
import { QuestionHistoryService } from './questionhistory.service';
import { QuestionHistoryComponent } from './questionhistory.component';
import { QuestionHistoryDetailComponent } from './questionhistory-detail.component';
import { QuestionHistoryUpdateComponent } from './questionhistory-update.component';
import { QuestionHistoryDeletePopupComponent } from './questionhistory-delete-dialog.component';
import { IQuestionHistory } from 'app/shared/model/questionhistory.model';

@Injectable({ providedIn: 'root' })
export class QuestionHistoryResolve implements Resolve<IQuestionHistory> {
    constructor(private service: QuestionHistoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((questionhistory: HttpResponse<QuestionHistory>) => questionhistory.body));
        }
        return of(new QuestionHistory());
    }
}

export const questionhistoryRoute: Routes = [
    {
        path: 'questionhistory',
        component: QuestionHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'questionhistory/:id/view',
        component: QuestionHistoryDetailComponent,
        resolve: {
            questionhistory: QuestionHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'questionhistory/new',
        component: QuestionHistoryUpdateComponent,
        resolve: {
            questionhistory: QuestionHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'questionhistory/:id/edit',
        component: QuestionHistoryUpdateComponent,
        resolve: {
            questionhistory: QuestionHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const questionhistoryPopupRoute: Routes = [
    {
        path: 'questionhistory/:id/delete',
        component: QuestionHistoryDeletePopupComponent,
        resolve: {
            questionhistory: QuestionHistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
