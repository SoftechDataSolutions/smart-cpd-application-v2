import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompanyRequest } from 'app/shared/model/company-request.model';

type EntityResponseType = HttpResponse<ICompanyRequest>;
type EntityArrayResponseType = HttpResponse<ICompanyRequest[]>;

@Injectable({ providedIn: 'root' })
export class CompanyRequestService {
    private resourceUrl = SERVER_API_URL + 'api/company-requests';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/company-requests';

    constructor(private http: HttpClient) {}

    create(companyRequest: ICompanyRequest): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(companyRequest);
        return this.http
            .post<ICompanyRequest>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(companyRequest: ICompanyRequest): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(companyRequest);
        return this.http
            .put<ICompanyRequest>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICompanyRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICompanyRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICompanyRequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(companyRequest: ICompanyRequest): ICompanyRequest {
        const copy: ICompanyRequest = Object.assign({}, companyRequest, {
            cycledate: companyRequest.cycledate != null && companyRequest.cycledate.isValid() ? companyRequest.cycledate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.cycledate = res.body.cycledate != null ? moment(res.body.cycledate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((companyRequest: ICompanyRequest) => {
            companyRequest.cycledate = companyRequest.cycledate != null ? moment(companyRequest.cycledate) : null;
        });
        return res;
    }
}
