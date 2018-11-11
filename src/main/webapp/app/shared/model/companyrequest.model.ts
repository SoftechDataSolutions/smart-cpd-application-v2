export interface ICompanyrequest {
    id?: number;
}

export class Companyrequest implements ICompanyrequest {
    constructor(public id?: number) {}
}
