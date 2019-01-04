import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model//customer.model';
import { ICourse } from 'app/shared/model//course.model';

export interface ITimeCourseLog {
    id?: number;
    loggedin?: Moment;
    loggedout?: Moment;
    timespent?: number;
    customer?: ICustomer;
    course?: ICourse;
}

export class TimeCourseLog implements ITimeCourseLog {
    constructor(
        public id?: number,
        public loggedin?: Moment,
        public loggedout?: Moment,
        public timespent?: number,
        public customer?: ICustomer,
        public course?: ICourse
    ) {}
}
