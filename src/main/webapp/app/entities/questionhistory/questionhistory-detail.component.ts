import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionhistory } from 'app/shared/model/questionhistory.model';

@Component({
    selector: 'jhi-questionhistory-detail',
    templateUrl: './questionhistory-detail.component.html'
})
export class QuestionhistoryDetailComponent implements OnInit {
    questionhistory: IQuestionhistory;

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
