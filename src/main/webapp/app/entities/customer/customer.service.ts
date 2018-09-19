import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomer } from 'app/shared/model/customer.model';

type EntityResponseType = HttpResponse<ICustomer>;
type EntityArrayResponseType = HttpResponse<ICustomer[]>;

@Injectable({ providedIn: 'root' })
export class CustomerService {
    private resourceUrl = SERVER_API_URL + 'api/customers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/customers';
    private resourceUserUrl = SERVER_API_URL + 'api/user/customers';

    constructor(private http: HttpClient) {}

    create(customer: ICustomer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(customer);
        return this.http
            .post<ICustomer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(customer: ICustomer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(customer);
        return this.http
            .put<ICustomer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getuser(id: number): Observable<ICustomer> {
        return this.http.get<ICustomer>(`${this.resourceUserUrl}/${id}`);
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomer[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(customer: ICustomer): ICustomer {
        const copy: ICustomer = Object.assign({}, customer, {
            registered: customer.registered != null && customer.registered.isValid() ? customer.registered.toJSON() : null,
            lastactive: customer.lastactive != null && customer.lastactive.isValid() ? customer.lastactive.toJSON() : null,
            cycledate: customer.cycledate != null && customer.cycledate.isValid() ? customer.cycledate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.registered = res.body.registered != null ? moment(res.body.registered) : null;
        res.body.lastactive = res.body.lastactive != null ? moment(res.body.lastactive) : null;
        res.body.cycledate = res.body.cycledate != null ? moment(res.body.cycledate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((customer: ICustomer) => {
            customer.registered = customer.registered != null ? moment(customer.registered) : null;
            customer.lastactive = customer.lastactive != null ? moment(customer.lastactive) : null;
            customer.cycledate = customer.cycledate != null ? moment(customer.cycledate) : null;
        });
        return res;
    }
}
