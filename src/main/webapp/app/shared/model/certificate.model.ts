import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model//customer.model';
import { ICourse } from 'app/shared/model//course.model';

export interface ICertificate {
    id?: number;
    timestamp?: Moment;
    isEmailed?: boolean;
    customer?: ICustomer;
    courses?: ICourse;
}

export class Certificate implements ICertificate {
    constructor(
        public id?: number,
        public timestamp?: Moment,
        public isEmailed?: boolean,
        public customer?: ICustomer,
        public courses?: ICourse
    ) {
        this.isEmailed = this.isEmailed || false;
    }
}
