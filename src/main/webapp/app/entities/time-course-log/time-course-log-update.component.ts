import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ITimeCourseLog } from 'app/shared/model/time-course-log.model';
import { TimeCourseLogService } from './time-course-log.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course';

@Component({
    selector: 'jhi-time-course-log-update',
    templateUrl: './time-course-log-update.component.html'
})
export class TimeCourseLogUpdateComponent implements OnInit {
    private _timeCourseLog: ITimeCourseLog;
    isSaving: boolean;

    customers: ICustomer[];

    courses: ICourse[];
    recorddate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private timeCourseLogService: TimeCourseLogService,
        private customerService: CustomerService,
        private courseService: CourseService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ timeCourseLog }) => {
            this.timeCourseLog = timeCourseLog;
        });
        this.customerService.query().subscribe(
            (res: HttpResponse<ICustomer[]>) => {
                this.customers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.courseService.query().subscribe(
            (res: HttpResponse<ICourse[]>) => {
                this.courses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.timeCourseLog.recorddate = moment(this.recorddate, DATE_TIME_FORMAT);
        if (this.timeCourseLog.id !== undefined) {
            this.subscribeToSaveResponse(this.timeCourseLogService.update(this.timeCourseLog));
        } else {
            this.subscribeToSaveResponse(this.timeCourseLogService.create(this.timeCourseLog));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITimeCourseLog>>) {
        result.subscribe((res: HttpResponse<ITimeCourseLog>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }

    trackCourseById(index: number, item: ICourse) {
        return item.id;
    }
    get timeCourseLog() {
        return this._timeCourseLog;
    }

    set timeCourseLog(timeCourseLog: ITimeCourseLog) {
        this._timeCourseLog = timeCourseLog;
        this.recorddate = moment(timeCourseLog.recorddate).format(DATE_TIME_FORMAT);
    }
}
