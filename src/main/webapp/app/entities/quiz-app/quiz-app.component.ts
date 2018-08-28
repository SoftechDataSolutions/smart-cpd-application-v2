import { Component, OnInit, OnDestroy, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IQuizApp } from 'app/shared/model/quiz-app.model';
import { Principal } from 'app/core';

import { QuizAppService } from './quiz-app.service';
import { IQuiz } from 'app/shared/model/quiz.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IQuestion } from 'app/shared/model/question.model';
import { IChoice } from 'app/shared/model/choice.model';
import { QuizService } from 'app/entities/quiz';
import { CustomerService } from 'app/entities/customer';
import { SectionService } from 'app/entities/section';
import { ISection } from 'app/shared/model/section.model';
import { QuestionHistoryService } from 'app/entities/question-history';
import { QuizHistoryService } from 'app/entities/quiz-history';
import { IQuizHistory } from 'app/shared/model/quiz-history.model';
import { IQuestionHistory } from 'app/shared/model/question-history.model';
import moment = require('moment');
import * as path from 'path';

@Component({
    selector: 'jhi-quiz-app',
    templateUrl: './quiz-app.component.html'
})
export class QuizAppComponent implements OnInit, OnDestroy {
    @ViewChildren('checkboxes') checkboxes: QueryList<ElementRef>;

    quizApp: IQuizApp;
    quiz: IQuiz;
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    customer: ICustomer;
    timer: any;
    startTime: Date;
    ellapsedTime = '00:00';
    questions: IQuestion[];
    numberQuestions: number;
    links: any;
    totalItems: number;
    choices: IChoice[];
    iscorrect: Boolean;
    isselected: Boolean;
    proceed: Boolean;
    restudy: string;
    message: string;
    currentQuestion: number;
    currSection: ISection;
    nextSection: ISection;
    quizHistoryInstance: IQuizHistory;
    questionHistoryInstance: IQuestionHistory;

    constructor(
        private quizAppService: QuizAppService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private quizService: QuizService,
        private customerService: CustomerService,
        private sectionService: SectionService,
        private questionHistoryService: QuestionHistoryService,
        private quizHistoryService: QuizHistoryService,
        private router: Router
    ) {
        this.iscorrect = false;
        this.isselected = true;
        this.proceed = false;
        this.currentQuestion = 1;
        this.isselected = false;
        this.proceed = false;
    }

    onSelect(question: IQuestion, option: IChoice) {
        question.choices.forEach(x => {
            if (x.id === option.id) {
                this.isselected = true;
            }
        });
        if (this.isselected) {
            if (option.isanswer) {
                this.proceed = true;
                this.message = 'Q-' + this.currentQuestion + ' has been answered correctly';
                this.currentQuestion++;
                this.isselected = false;
                this.questionHistoryInstance.correct = true;
                this.questionHistoryInstance.customer = this.customer;
                this.questionHistoryInstance.question = question;
                this.questionHistoryInstance.timestamp = moment();
                this.questionHistoryService.create(this.questionHistoryInstance);
                return true;
            } else {
                this.proceed = false;
                this.message =
                    'You have selected the wrong option for this question, kindly review the following the restudy material to attempt again and proceed further';
                this.restudy = question.restudy;
                this.clearOptions();
                this.isselected = false;
                this.questionHistoryInstance.correct = false;
                this.questionHistoryInstance.customer = this.customer;
                this.questionHistoryInstance.question = question;
                this.questionHistoryInstance.timestamp = moment();
                this.questionHistoryService.create(this.questionHistoryInstance);
                return false;
            }
        } else {
            this.message = 'You have not selected any options for this question';
            this.proceed = false;
            return false;
        }
    }

    goToNext() {
        if (this.isLastQuestion()) {
            this.router.navigate(['/section', this.nextSection]);
        } else {
            return false;
        }
    }

    passedQuiz() {
        this.quizHistoryInstance.customer = this.customer;
        this.quizHistoryInstance.quiz = this.quiz;
        this.quizHistoryInstance.passed = true;
        this.quizHistoryInstance.start = moment();
        this.quizHistoryService.create(this.quizHistoryInstance);
    }

    isLastQuestion() {
        if (this.currentQuestion === this.questions.length) {
            return true;
        } else {
            return false;
        }
    }

    clearOptions() {
        this.checkboxes.forEach(element => {
            element.nativeElement.checked = false;
        });
    }

    addNew() {
        this.router.navigate(['/quiz-app/new']);
    }

    /**
     * Custom Methods End
     */
    tick() {
        const now = new Date();
        const diff = (now.getTime() - this.startTime.getTime()) / 1000;
        this.ellapsedTime = this.parseTime(diff);
    }

    parseTime(totalSeconds: number) {
        let mins: string | number = Math.floor(totalSeconds / 60);
        let secs: string | number = Math.round(totalSeconds % 60);
        mins = (mins < 10 ? '0' : '') + mins;
        secs = (secs < 10 ? '0' : '') + secs;
        return `${mins}:${secs}`;
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ quizApp }) => {
            this.quizApp = quizApp;
        });
        this.quiz = this.quizService.find(this.quizApp.quiz.id).flatMap[0];
        this.customer = this.customerService.find(this.quizApp.customer.id).flatMap[0];
        this.currSection = this.sectionService.find(this.quizApp.currSection.id).flatMap[0];
        this.nextSection = this.sectionService.find(this.quizApp.newSection.id).flatMap[0];
        this.numberQuestions = this.quiz.questions.length;
        for (let i = 0; i < this.numberQuestions; i++) {
            this.questions.push(this.quiz.questions[i]);
        }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IQuizApp) {
        return item.id;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
