import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model//customer.model';
import { ICourse } from 'app/shared/model//course.model';

export interface ITimeCourseLog {
    id?: number;
    timespent?: number;
    recorddate?: Moment;
    customer?: ICustomer;
    course?: ICourse;
}

export class TimeCourseLog implements ITimeCourseLog {
    constructor(
        public id?: number,
        public timespent?: number,
        public recorddate?: Moment,
        public customer?: ICustomer,
        public course?: ICourse
    ) {}
}
