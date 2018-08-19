import { Moment } from 'moment';
import { ICompany } from 'app/shared/model//company.model';

export const enum TYPES {
    RESIDENCE = 'RESIDENCE',
    COMMERCIAL = 'COMMERCIAL',
    INDUSTRIAL = 'INDUSTRIAL'
}

export interface ICustomer {
    id?: number;
    normalized?: string;
    phone?: string;
    streetaddress?: string;
    postalcode?: string;
    city?: string;
    stateProvince?: string;
    country?: string;
    profilePicContentType?: string;
    profilePic?: any;
    registered?: Moment;
    lastactive?: Moment;
    points?: number;
    cycledate?: Moment;
    areaserviced?: string;
    specialities?: TYPES;
    trades?: string;
    monthYear?: string;
    licenseNumber?: string;
    company?: ICompany;
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public normalized?: string,
        public phone?: string,
        public streetaddress?: string,
        public postalcode?: string,
        public city?: string,
        public stateProvince?: string,
        public country?: string,
        public profilePicContentType?: string,
        public profilePic?: any,
        public registered?: Moment,
        public lastactive?: Moment,
        public points?: number,
        public cycledate?: Moment,
        public areaserviced?: string,
        public specialities?: TYPES,
        public trades?: string,
        public monthYear?: string,
        public licenseNumber?: string,
        public company?: ICompany
    ) {}
}
