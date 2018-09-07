import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { ISection } from 'app/shared/model/section.model';
import { VgAPI } from 'videogular2/core';
import { BookmarkService } from 'app/entities/bookmark';
import { IBookmark } from 'app/shared/model/bookmark.model';
import { interval } from 'rxjs';
import { templateJitUrl } from '@angular/compiler';
import { numberOfBytes } from 'ng-jhipster/src/directive/number-of-bytes';

@Component({
    selector: 'jhi-section-detail',
    templateUrl: './section-detail.component.html'
})
export class SectionDetailComponent implements OnInit {
    section: ISection;
    pageNum = 1;
    lastpageNum: number;
    test: any;
    pdflink: string;
    quizName: string;
    courseName: string;
    api: VgAPI;
    ellapsedTime = '00:00';
    ellapsed = 0;
    prevEllapsed = 0;
    bookmarks: IBookmark[];
    reqdBookmarks: IBookmark[];
    pause = false;
    startDate: Date;
    nowDate: Date;
    counter = 0;
    diff: any;
    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private bookmarkService: BookmarkService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ section }) => {
            this.section = section;
        });
        this.startDate = new Date();
        if (this.section.type === 'pdf') {
            setInterval(() => {
                this.ticksDate();
            }, 1000);
        } else {
            setInterval(() => {
                this.ticksSecond();
            }, 1000);
        }
        /**this.contentFile = new Uint8Array(this.section.content.length);
        for (let i = 0; i < this.section.content.length; i++) {
            this.contentFile[i] = this.section.content.charCodeAt(i);
        }*/
        this.pdflink = this.section.pdfUrl;
        this.lastpageNum = this.section.totalPages;
        this.bookmarkService.getsection(this.section.name).subscribe(data => {
            this.bookmarks = data.body;
        });
        this.quizName = this.section.quiz.name;
        this.courseName = this.section.course.normCourses;
    }

    onPause() {
        this.pause = true;
        this.api.pause();
    }

    referSlide(slide: number) {
        this.pageNum = slide;
    }

    referVideo(time: string) {
        const hrs = time.substring(0, 2);
        const min = time.substring(3, 5);
        const sec = time.substring(6, 8);
        const hrsNum = Number(hrs);
        const minNum = Number(min);
        const secNum = Number(sec);
        this.api.seekTime(hrsNum * 3600 + minNum * 60 + secNum);
    }

    onPlay() {
        this.prevEllapsed = this.ellapsed;
        this.startDate = new Date();
        this.pause = false;
        this.api.play();
    }

    onStepBackward30() {
        if (this.ellapsed <= 30) {
            this.onReset();
        } else {
            /**this.ellapsed = this.ellapsed - 30;*/
            this.api.seekTime(this.ellapsed - 30, false);
        }
    }

    onStepBackward60() {
        if (this.ellapsed <= 60) {
            this.onReset();
        } else {
            /**this.ellapsed = this.ellapsed - 60;*/
            this.api.seekTime(this.ellapsed - 60, false);
        }
    }

    onStepBackward300() {
        if (this.ellapsed <= 300) {
            this.onReset();
        } else {
            /**this.ellapsed = this.ellapsed - 300;*/
            this.api.seekTime(this.ellapsed - 300, false);
        }
    }

    onReset() {
        this.api.seekTime(0, false);
    }

    ticksDate() {
        if (this.pause === false) {
            this.nowDate = new Date();
            this.ellapsed = (this.nowDate.getTime() - this.startDate.getTime()) / 1000 + this.prevEllapsed;
            this.ellapsedTime = this.parseTime(this.ellapsed);
        }
    }

    ticksSecond() {
        if (this.api.isCompleted) {
            this.pause = true;
        } else {
            if (this.pause === false) {
                this.ellapsed++;
                this.ellapsedTime = this.parseTime(this.ellapsed);
                /**this.ellapsed = this.api.currentTime;
                 this.ellapsedTime = this.parseTime(this.ellapsed);*/
            }
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

    gotoSlide(slide: number) {
        this.pageNum = slide;
    }

    onPlayerReady(api: VgAPI) {
        this.api = api;
    }

    nextPage() {
        this.pageNum++;
    }

    prevPage() {
        this.pageNum--;
    }

    moveToQuiz() {
        this.router.navigateByUrl('quiz/' + this.section.quiz.id.toString() + '/view');
    }
}
