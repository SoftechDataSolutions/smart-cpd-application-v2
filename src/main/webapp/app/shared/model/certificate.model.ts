import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model//customer.model';

export interface ICertificate {
    id?: number;
    timestamp?: Moment;
    firstname?: string;
    lastname?: string;
    course?: string;
    contentContentType?: string;
    content?: any;
    customer?: ICustomer;
}

export class Certificate implements ICertificate {
    constructor(
        public id?: number,
        public timestamp?: Moment,
        public firstname?: string,
        public lastname?: string,
        public course?: string,
        public contentContentType?: string,
        public content?: any,
        public customer?: ICustomer
    ) {}
}
