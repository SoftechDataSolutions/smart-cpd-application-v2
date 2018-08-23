import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Questionhistory } from 'app/shared/model/questionhistory.model';
import { QuestionhistoryService } from './questionhistory.service';
import { QuestionhistoryComponent } from './questionhistory.component';
import { QuestionhistoryDetailComponent } from './questionhistory-detail.component';
import { QuestionhistoryUpdateComponent } from './questionhistory-update.component';
import { QuestionhistoryDeletePopupComponent } from './questionhistory-delete-dialog.component';
import { IQuestionhistory } from 'app/shared/model/questionhistory.model';

@Injectable({ providedIn: 'root' })
export class QuestionhistoryResolve implements Resolve<IQuestionhistory> {
    constructor(private service: QuestionhistoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((questionhistory: HttpResponse<Questionhistory>) => questionhistory.body));
        }
        return of(new Questionhistory());
    }
}

export const questionhistoryRoute: Routes = [
    {
        path: 'questionhistory',
        component: QuestionhistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'questionhistory/:id/view',
        component: QuestionhistoryDetailComponent,
        resolve: {
            questionhistory: QuestionhistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'questionhistory/new',
        component: QuestionhistoryUpdateComponent,
        resolve: {
            questionhistory: QuestionhistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'questionhistory/:id/edit',
        component: QuestionhistoryUpdateComponent,
        resolve: {
            questionhistory: QuestionhistoryResolve
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
        component: QuestionhistoryDeletePopupComponent,
        resolve: {
            questionhistory: QuestionhistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.questionhistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
