import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICart } from 'app/shared/model/cart.model';
import { ICustomer } from 'app/shared/model/customer.model';

type EntityResponseType = HttpResponse<ICart>;
type EntityArrayResponseType = HttpResponse<ICart[]>;

@Injectable({ providedIn: 'root' })
export class CartService {
    private resourceUrl = SERVER_API_URL + 'api/carts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/carts';
    private resourceCheckUrl = SERVER_API_URL + 'api/_check/carts';

    constructor(private http: HttpClient) {}

    create(cart: ICart): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cart);
        return this.http
            .post<ICart>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(cart: ICart): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cart);
        return this.http
            .put<ICart>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICart>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    check(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICart>(`${this.resourceCheckUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICart[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICart[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(cart: ICart): ICart {
        const copy: ICart = Object.assign({}, cart, {
            createddate: cart.createddate != null && cart.createddate.isValid() ? cart.createddate.toJSON() : null,
            lastactivedate: cart.lastactivedate != null && cart.lastactivedate.isValid() ? cart.lastactivedate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createddate = res.body.createddate != null ? moment(res.body.createddate) : null;
        res.body.lastactivedate = res.body.lastactivedate != null ? moment(res.body.lastactivedate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((cart: ICart) => {
            cart.createddate = cart.createddate != null ? moment(cart.createddate) : null;
            cart.lastactivedate = cart.lastactivedate != null ? moment(cart.lastactivedate) : null;
        });
        return res;
    }
}
