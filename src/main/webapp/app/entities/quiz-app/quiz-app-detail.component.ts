import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuizApp } from 'app/shared/model/quiz-app.model';
import { IQuestion } from 'app/shared/model/question.model';
import { QuizAppService } from 'app/entities/quiz-app/quiz-app.service';
import { QuestionService } from 'app/entities/question';

@Component({
    selector: 'jhi-quiz-app-detail',
    templateUrl: './quiz-app-detail.component.html'
})
export class QuizAppDetailComponent implements OnInit {
    quizApp: IQuizApp;
    questions: IQuestion[];

    constructor(private activatedRoute: ActivatedRoute, private quizappService: QuizAppService, private questionService: QuestionService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ quizApp }) => {
            this.quizApp = quizApp;
        });
        this.questionService.findbyquiz(this.quizApp.quiz.id).map(result => {
            this.questions = result.body;
        });
    }

    previousState() {
        window.history.back();
    }
}
