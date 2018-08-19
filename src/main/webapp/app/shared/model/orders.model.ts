import { Moment } from 'moment';
import { ICart } from 'app/shared/model//cart.model';

export const enum NOTIFICATIONS {
    ORDERPROCESSING = 'ORDERPROCESSING',
    COMPLETE = 'COMPLETE',
    CANCELLLED = 'CANCELLLED',
    REFUND = 'REFUND',
    ONHOLD = 'ONHOLD'
}

export const enum PAYMENT {
    PAYPAL = 'PAYPAL',
    STRIPE = 'STRIPE'
}

export interface IOrders {
    id?: number;
    createddate?: Moment;
    amount?: number;
    status?: NOTIFICATIONS;
    payment?: PAYMENT;
    cart?: ICart;
}

export class Orders implements IOrders {
    constructor(
        public id?: number,
        public createddate?: Moment,
        public amount?: number,
        public status?: NOTIFICATIONS,
        public payment?: PAYMENT,
        public cart?: ICart
    ) {}
}
