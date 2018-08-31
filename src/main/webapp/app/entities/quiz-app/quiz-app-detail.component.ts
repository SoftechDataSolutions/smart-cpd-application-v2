import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuizApp } from 'app/shared/model/quiz-app.model';

@Component({
    selector: 'jhi-quiz-app-detail',
    templateUrl: './quiz-app-detail.component.html'
})
export class QuizAppDetailComponent implements OnInit {
    quizApp: IQuizApp;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ quizApp }) => {
            this.quizApp = quizApp;
        });
    }

    previousState() {
        window.history.back();
    }
}
