import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { ISection } from 'app/shared/model/section.model';
import { VgAPI } from 'videogular2/core';

@Component({
    selector: 'jhi-section-detail',
    templateUrl: './section-detail.component.html'
})
export class SectionDetailComponent implements OnInit {
    section: ISection;
    contentFile: Uint8Array;
    pageNum: number;
    lastpageNum: number;
    test: any;
    pdfUrl: string;
    quizName: string;
    courseName: string;
    api: VgAPI;
    ellapsedTime = '00:00';
    startSec: number;
    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute, private router: Router) {
        this.pageNum = 1;
        this.startSec = 0;
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ section }) => {
            this.section = section;
        });
        /**this.contentFile = new Uint8Array(this.section.content.length);
        for (let i = 0; i < this.contentFile.length; i++) {
            this.contentFile[i] = this.section.content.charCodeAt(i);
        }
        const blob = new Blob([this.contentFile], { type: this.section.contentContentType });
        this.pdfUrl = this.section.pdfUrl;*/
        this.lastpageNum = this.section.totalPages;
        this.quizName = this.section.quiz.name;
        this.courseName = this.section.course.normCourses;
        this.timeIncrement();
        /**const fileReader: FileReader = new FileReader();
        fileReader.onload = () => {
            this.contentFile = new Uint8Array(fileReader.result);
        };
        fileReader.readAsArrayBuffer(this.section.content);
        if (typeof (FileReader) !== 'undefined') {
            const reader = new FileReader();

            reader.onload = (e: any) => {
                this.contentFile = e.target.result;
            };

            reader.readAsArrayBuffer(this.section.content);*/
    }

    onPlayerReady(api: VgAPI) {
        this.api = api;
    }

    timeIncrement() {
        this.startSec++;
        this.ellapsedTime = this.parseTime(this.startSec);
    }

    parseTime(totalSeconds: number) {
        let mins: string | number = Math.floor(totalSeconds / 60);
        let secs: string | number = Math.round(totalSeconds % 60);
        mins = (mins < 10 ? '0' : '') + mins;
        secs = (secs < 10 ? '0' : '') + secs;
        return `${mins}:${secs}`;
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

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
