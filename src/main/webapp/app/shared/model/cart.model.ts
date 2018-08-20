import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model//customer.model';

export interface ICart {
    id?: number;
    normCart?: string;
    createddate?: Moment;
    lastactivedate?: Moment;
    amount?: number;
    checkout?: boolean;
    customer?: ICustomer;
}

export class Cart implements ICart {
    constructor(
        public id?: number,
        public normCart?: string,
        public createddate?: Moment,
        public lastactivedate?: Moment,
        public amount?: number,
        public checkout?: boolean,
        public customer?: ICustomer
    ) {
        this.checkout = this.checkout || false;
    }
}
