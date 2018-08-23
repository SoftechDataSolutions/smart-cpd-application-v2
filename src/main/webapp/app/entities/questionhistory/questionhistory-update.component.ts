import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IQuestionhistory } from 'app/shared/model/questionhistory.model';
import { QuestionhistoryService } from './questionhistory.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { IQuestion } from 'app/shared/model/question.model';
import { QuestionService } from 'app/entities/question';
import { IChoice } from 'app/shared/model/choice.model';
import { ChoiceService } from 'app/entities/choice';

@Component({
    selector: 'jhi-questionhistory-update',
    templateUrl: './questionhistory-update.component.html'
})
export class QuestionhistoryUpdateComponent implements OnInit {
    private _questionhistory: IQuestionhistory;
    isSaving: boolean;

    customers: ICustomer[];

    questions: IQuestion[];

    choices: IChoice[];
    timestamp: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private questionhistoryService: QuestionhistoryService,
        private customerService: CustomerService,
        private questionService: QuestionService,
        private choiceService: ChoiceService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ questionhistory }) => {
            this.questionhistory = questionhistory;
        });
        this.customerService.query().subscribe(
            (res: HttpResponse<ICustomer[]>) => {
                this.customers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.questionService.query().subscribe(
            (res: HttpResponse<IQuestion[]>) => {
                this.questions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.choiceService.query().subscribe(
            (res: HttpResponse<IChoice[]>) => {
                this.choices = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.questionhistory.timestamp = moment(this.timestamp, DATE_TIME_FORMAT);
        if (this.questionhistory.id !== undefined) {
            this.subscribeToSaveResponse(this.questionhistoryService.update(this.questionhistory));
        } else {
            this.subscribeToSaveResponse(this.questionhistoryService.create(this.questionhistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionhistory>>) {
        result.subscribe((res: HttpResponse<IQuestionhistory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }

    trackQuestionById(index: number, item: IQuestion) {
        return item.id;
    }

    trackChoiceById(index: number, item: IChoice) {
        return item.id;
    }
    get questionhistory() {
        return this._questionhistory;
    }

    set questionhistory(questionhistory: IQuestionhistory) {
        this._questionhistory = questionhistory;
        this.timestamp = moment(questionhistory.timestamp).format(DATE_TIME_FORMAT);
    }
}
