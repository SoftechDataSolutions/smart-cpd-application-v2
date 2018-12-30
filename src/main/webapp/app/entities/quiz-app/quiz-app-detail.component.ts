import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IQuizApp } from 'app/shared/model/quiz-app.model';
import { IQuiz } from 'app/shared/model/quiz.model';
import { IQuestion } from 'app/shared/model/question.model';
import { IChoice } from 'app/shared/model/choice.model';
import { CustomerService } from 'app/entities/customer';
import { IBookmark } from 'app/shared/model/bookmark.model';
import { ICourse } from 'app/shared/model/course.model';
import { ITimeCourseLog } from 'app/shared/model/time-course-log.model';
import { TimeCourseLogService } from 'app/entities/time-course-log';
import { Account, Principal, UserService } from 'app/core';
import { SectionHistoryService } from 'app/entities/section-history';
import { ISectionHistory } from 'app/shared/model/section-history.model';
import { QuizHistoryService } from 'app/entities/quiz-history';
import { IQuizHistory } from 'app/shared/model/quiz-history.model';
import moment = require('moment');
import { CertificateService } from 'app/entities/certificate';
import { ICertificate } from 'app/shared/model/certificate.model';
import { ICustomer } from 'app/shared/model/customer.model';

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
    ellapsed = 0;
    ellapsedTime: string;
    prevEllapsed = 0;
    pause = false;
    startDate: Date;
    nowDate: Date;
    account: Account;
    custEmail: string;
    reqdCourse: ICourse;
    logs: ITimeCourseLog;
    tempQuizHist: IQuizHistory;
    currentAccount: any;
    customer: ICustomer;
    certificate: ICertificate;
    @ViewChildren('radioBtn') checkboxes: QueryList<ElementRef>;
    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private userService: UserService,
        private customerService: CustomerService,
        private timeCourseLogService: TimeCourseLogService,
        private principal: Principal,
        private sectionHistoryService: SectionHistoryService,
        private quizHistoryService: QuizHistoryService,
        private certificateService: CertificateService
    ) {
        this.currentQuestion = 1;
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ quizApp }) => {
            this.quizApp = quizApp;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.custEmail = this.currentAccount.email;
        });
        this.quiz = this.quizApp.quiz;
        this.questionList = this.quizApp.questions;
        this.currentQuestion = 1;
        this.isselected = false;
        this.proceed = false;
        this.clearOptions();
        this.startDate = new Date();
        setInterval(() => {
            this.ticksDate();
            this.value = (this.currentQuestion - 1) / this.questionList.length * 100;
        }, 1000);
        this.tempQuizHist.start = moment();
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
                    this.userService.getemail(this.custEmail).subscribe(userId => {
                        this.customerService.getuser(userId).subscribe(customer => {
                            this.reqdCourse = this.quizApp.currSection.course;
                            this.logs.timespent = this.ellapsed;
                            this.logs.course = this.reqdCourse;
                            this.logs.customer = customer;
                            this.timeCourseLogService.create(this.logs).subscribe(() => {
                                this.tempQuizHist.customer = customer;
                                this.tempQuizHist.quiz = this.quiz;
                                this.tempQuizHist.passed = true;
                                this.quizHistoryService.create(this.tempQuizHist);
                            });
                        });
                    });
                    this.message =
                        'You have successfully completed the' +
                        this.quizApp.quiz.name +
                        'quiz, and shortly you will be redirected to Course:' +
                        this.quizApp.currSection.course.normCourses +
                        ', Module: ' +
                        this.quizApp.newSection.normSection;
                    if (this.quizApp.newSection !== null) {
                        setTimeout(
                            function() {
                                this.router.navigateByUrl('section/' + this.quizApp.newSection.id.toString() + '/view');
                            }.bind(this),
                            6000
                        );
                    } else {
                        this.userService.getemail(this.custEmail).subscribe(userId => {
                            this.customerService.getuser(userId).subscribe(customer => {
                                this.customer = customer;
                                this.certificate.courses = this.reqdCourse;
                                this.certificate.timestamp = moment();
                                this.certificate.customer = this.customer;
                                this.certificateService.create(this.certificate).subscribe(() => {
                                    setTimeout(
                                        function() {
                                            this.router.navigateByUrl('certificate/' + this.certificate.id.toString() + '/view');
                                        }.bind(this),
                                        6000
                                    );
                                });
                            });
                        });
                    }
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

    ticksDate() {
        if (this.pause === false) {
            this.nowDate = new Date();
            this.ellapsed = (this.nowDate.getTime() - this.startDate.getTime()) / 1000;
            this.ellapsedTime = this.parseTime(this.ellapsed);
        }
    }

    parseTime(totalSeconds: number) {
        let hrs: string | number = Math.floor(totalSeconds / 3600);
        if (Number(hrs) < 1) {
            let mins: string | number = Math.floor(totalSeconds / 60);
            let secs: string | number = Math.round(totalSeconds % 60);
            mins = (mins < 10 ? '0' : '') + mins;
            secs = (secs < 10 ? '0' : '') + secs;
            hrs = (hrs < 10 ? '0' : '') + hrs;
            return mins + ':' + secs;
        } else {
            let mins: string | number = Math.floor((totalSeconds % 3600) / 60);
            let secs: string | number = Math.round(totalSeconds % 60);
            mins = (mins < 10 ? '0' : '') + mins;
            secs = (secs < 10 ? '0' : '') + secs;
            hrs = (hrs < 10 ? '0' : '') + hrs;
            return hrs + ':' + mins + ':' + secs;
        }
    }
}
