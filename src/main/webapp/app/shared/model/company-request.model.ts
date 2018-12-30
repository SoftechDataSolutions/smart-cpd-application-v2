import { Moment } from 'moment';

export interface ICompanyRequest {
    id?: number;
    name?: string;
    description?: string;
    phone?: string;
    email?: string;
    streetAddress?: string;
    postalCode?: string;
    city?: string;
    stateProvince?: string;
    country?: string;
    cycledate?: Moment;
    url?: string;
}

export class CompanyRequest implements ICompanyRequest {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public phone?: string,
        public email?: string,
        public streetAddress?: string,
        public postalCode?: string,
        public city?: string,
        public stateProvince?: string,
        public country?: string,
        public cycledate?: Moment,
        public url?: string
    ) {}
}
