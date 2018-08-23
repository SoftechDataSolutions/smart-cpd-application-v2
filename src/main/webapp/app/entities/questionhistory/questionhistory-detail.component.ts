import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionHistory } from 'app/shared/model/questionhistory.model';

@Component({
    selector: 'jhi-questionhistory-detail',
    templateUrl: './questionhistory-detail.component.html'
})
export class QuestionHistoryDetailComponent implements OnInit {
    questionhistory: IQuestionHistory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ questionhistory }) => {
            this.questionhistory = questionhistory;
        });
    }

    previousState() {
        window.history.back();
    }
}
