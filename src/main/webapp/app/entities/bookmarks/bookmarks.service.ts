import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBookmarks } from 'app/shared/model/bookmarks.model';

type EntityResponseType = HttpResponse<IBookmarks>;
type EntityArrayResponseType = HttpResponse<IBookmarks[]>;

@Injectable({ providedIn: 'root' })
export class BookmarksService {
    private resourceUrl = SERVER_API_URL + 'api/bookmarks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bookmarks';

    constructor(private http: HttpClient) {}

    create(bookmarks: IBookmarks): Observable<EntityResponseType> {
        return this.http.post<IBookmarks>(this.resourceUrl, bookmarks, { observe: 'response' });
    }

    update(bookmarks: IBookmarks): Observable<EntityResponseType> {
        return this.http.put<IBookmarks>(this.resourceUrl, bookmarks, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBookmarks>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBookmarks[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBookmarks[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
