import { Moment } from 'moment';

export interface ICompany {
    id?: number;
    name?: string;
    description?: string;
    notes?: string;
    phone?: string;
    streetAddress?: string;
    postalCode?: string;
    city?: string;
    stateProvince?: string;
    country?: string;
    cycledate?: Moment;
}

export class Company implements ICompany {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public notes?: string,
        public phone?: string,
        public streetAddress?: string,
        public postalCode?: string,
        public city?: string,
        public stateProvince?: string,
        public country?: string,
        public cycledate?: Moment
    ) {}
}
