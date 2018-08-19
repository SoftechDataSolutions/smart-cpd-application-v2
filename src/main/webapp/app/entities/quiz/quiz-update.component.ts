import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IQuiz } from 'app/shared/model/quiz.model';
import { QuizService } from './quiz.service';

@Component({
    selector: 'jhi-quiz-update',
    templateUrl: './quiz-update.component.html'
})
export class QuizUpdateComponent implements OnInit {
    private _quiz: IQuiz;
    isSaving: boolean;

    constructor(private quizService: QuizService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ quiz }) => {
            this.quiz = quiz;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.quiz.id !== undefined) {
            this.subscribeToSaveResponse(this.quizService.update(this.quiz));
        } else {
            this.subscribeToSaveResponse(this.quizService.create(this.quiz));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IQuiz>>) {
        result.subscribe((res: HttpResponse<IQuiz>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get quiz() {
        return this._quiz;
    }

    set quiz(quiz: IQuiz) {
        this._quiz = quiz;
    }
}
