import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IQuizApp } from 'app/shared/model/quiz-app.model';
import { IQuiz } from 'app/shared/model/quiz.model';
import { IQuestion } from 'app/shared/model/question.model';
import { IChoice } from 'app/shared/model/choice.model';

@Component({
    selector: 'jhi-quiz-app-detail',
    templateUrl: './quiz-app-detail.component.html'
})
export class QuizAppDetailComponent implements OnInit {
    quizApp: IQuizApp;
    quiz: IQuiz;
    questionList: IQuestion[];
    currentQuestion: number;
    proceed: Boolean;
    isselected: Boolean;
    message: string;
    restudy: string;
    value: number;
    @ViewChildren('radioBtn') checkboxes: QueryList<ElementRef>;
    constructor(private activatedRoute: ActivatedRoute, private router: Router) {
        this.currentQuestion = 1;
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ quizApp }) => {
            this.quizApp = quizApp;
        });
        this.quiz = this.quizApp.quiz;
        this.questionList = this.quizApp.questions;
        this.currentQuestion = 1;
        this.isselected = false;
        this.proceed = false;
        this.clearOptions();
        this.value = (this.currentQuestion - 1) / this.questionList.length * 100;
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
                this.message = 'Q:' + this.currentQuestion + ' has been answered correctly, moving to the next question';
                if (this.currentQuestion >= this.questionList.length) {
                    this.value = this.currentQuestion / this.questionList.length * 100;
                }
                if (this.currentQuestion >= this.questionList.length) {
                    this.message =
                        'You have successfully completed the' +
                        this.quizApp.quiz.name +
                        'quiz, and shortly you will be redirected to Course:' +
                        this.quizApp.currSection.course.normCourses +
                        ', Module: ' +
                        this.quizApp.newSection.normSection;
                    setTimeout(
                        function() {
                            this.router.navigateByUrl('section/' + this.quizApp.newSection.id.toString() + '/view');
                        }.bind(this),
                        6000
                    );
                } else {
                    setTimeout(
                        function() {
                            this.currentQuestion++;
                            this.value = (this.currentQuestion - 1) / this.questionList.length * 100;
                            this.proceed = false;
                            this.isselected = false;
                            this.clearOptions();
                        }.bind(this),
                        2000
                    );
                }
                /**this.isselected = false;*/
                /**this.questionHistoryInstance.correct = true;
                this.questionHistoryInstance.customer = this.customer;
                this.questionHistoryInstance.question = question;
                this.questionHistoryInstance.timestamp = moment();
                this.questionHistoryService.create(this.questionHistoryInstance);*/
            } else {
                this.proceed = false;
                this.message =
                    'You have selected the wrong option for this question, kindly review the following the restudy material to attempt again and proceed further';
                this.restudy = question.restudy;
                setTimeout(
                    function() {
                        this.value = (this.currentQuestion - 1) / this.questionList.length * 100;
                        this.isselected = false;
                        this.clearOptions();
                    }.bind(this),
                    6000
                );
                /**this.isselected = false;*/
                /**this.questionHistoryInstance.correct = false;
                this.questionHistoryInstance.customer = this.customer;
                this.questionHistoryInstance.question = question;
                this.questionHistoryInstance.timestamp = moment();
                this.questionHistoryService.create(this.questionHistoryInstance);*/
            }
        } else {
            this.message = 'You have not selected any options for this question';
            this.proceed = false;
        }
    }

    clearOptions() {
        this.checkboxes.forEach(element => {
            element.nativeElement.checked = false;
        });
    }

    previousState() {
        window.history.back();
    }
}
