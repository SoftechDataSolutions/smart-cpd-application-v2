import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model//customer.model';
import { ICourse } from 'app/shared/model//course.model';

export interface ICertificate {
    id?: number;
    timestamp?: Moment;
    pdfContentType?: string;
    pdf?: any;
    customer?: ICustomer;
    courses?: ICourse;
}

export class Certificate implements ICertificate {
    constructor(
        public id?: number,
        public timestamp?: Moment,
        public pdfContentType?: string,
        public pdf?: any,
        public customer?: ICustomer,
        public courses?: ICourse
    ) {}
}
