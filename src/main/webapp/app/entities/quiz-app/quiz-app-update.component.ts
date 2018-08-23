import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IQuizApp } from 'app/shared/model/quiz-app.model';
import { QuizAppService } from './quiz-app.service';
import { IQuiz } from 'app/shared/model/quiz.model';
import { QuizService } from 'app/entities/quiz';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
    selector: 'jhi-quiz-app-update',
    templateUrl: './quiz-app-update.component.html'
})
export class QuizAppUpdateComponent implements OnInit {
    private _quizApp: IQuizApp;
    isSaving: boolean;

    quizzes: IQuiz[];

    customers: ICustomer[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private quizAppService: QuizAppService,
        private quizService: QuizService,
        private customerService: CustomerService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ quizApp }) => {
            this.quizApp = quizApp;
        });
        this.quizService.query().subscribe(
            (res: HttpResponse<IQuiz[]>) => {
                this.quizzes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.customerService.query().subscribe(
            (res: HttpResponse<ICustomer[]>) => {
                this.customers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.quizApp.id !== undefined) {
            this.subscribeToSaveResponse(this.quizAppService.update(this.quizApp));
        } else {
            this.subscribeToSaveResponse(this.quizAppService.create(this.quizApp));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IQuizApp>>) {
        result.subscribe((res: HttpResponse<IQuizApp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackQuizById(index: number, item: IQuiz) {
        return item.id;
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }
    get quizApp() {
        return this._quizApp;
    }

    set quizApp(quizApp: IQuizApp) {
        this._quizApp = quizApp;
    }
}
