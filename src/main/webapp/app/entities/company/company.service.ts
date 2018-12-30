import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompany } from 'app/shared/model/company.model';

type EntityResponseType = HttpResponse<ICompany>;
type EntityArrayResponseType = HttpResponse<ICompany[]>;

@Injectable({ providedIn: 'root' })
export class CompanyService {
    private resourceUrl = SERVER_API_URL + 'api/companies';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/companies';
    private resourceCompanyUrl = SERVER_API_URL + 'api/company';

    constructor(private http: HttpClient) {}

    create(company: ICompany): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(company);
        return this.http
            .post<ICompany>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(company: ICompany): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(company);
        return this.http
            .put<ICompany>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    createfromrequest(id: number): Observable<ICompany> {
        return this.http.post<ICompany>(`${this.resourceCompanyUrl}/${id}`, {});
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICompany>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICompany[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICompany[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(company: ICompany): ICompany {
        const copy: ICompany = Object.assign({}, company, {
            cycledate: company.cycledate != null && company.cycledate.isValid() ? company.cycledate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.cycledate = res.body.cycledate != null ? moment(res.body.cycledate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((company: ICompany) => {
            company.cycledate = company.cycledate != null ? moment(company.cycledate) : null;
        });
        return res;
    }
}
