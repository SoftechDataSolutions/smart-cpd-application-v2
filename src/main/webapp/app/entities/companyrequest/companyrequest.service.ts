import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompanyrequest } from 'app/shared/model/companyrequest.model';

type EntityResponseType = HttpResponse<ICompanyrequest>;
type EntityArrayResponseType = HttpResponse<ICompanyrequest[]>;

@Injectable({ providedIn: 'root' })
export class CompanyrequestService {
    private resourceUrl = SERVER_API_URL + 'api/companyrequests';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/companyrequests';

    constructor(private http: HttpClient) {}

    create(companyrequest: ICompanyrequest): Observable<EntityResponseType> {
        return this.http.post<ICompanyrequest>(this.resourceUrl, companyrequest, { observe: 'response' });
    }

    update(companyrequest: ICompanyrequest): Observable<EntityResponseType> {
        return this.http.put<ICompanyrequest>(this.resourceUrl, companyrequest, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICompanyrequest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompanyrequest[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompanyrequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
