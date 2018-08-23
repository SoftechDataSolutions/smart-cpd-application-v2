import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IQuestionhistory } from 'app/shared/model/questionhistory.model';

type EntityResponseType = HttpResponse<IQuestionhistory>;
type EntityArrayResponseType = HttpResponse<IQuestionhistory[]>;

@Injectable({ providedIn: 'root' })
export class QuestionhistoryService {
    private resourceUrl = SERVER_API_URL + 'api/questionhistories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/questionhistories';

    constructor(private http: HttpClient) {}

    create(questionhistory: IQuestionhistory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(questionhistory);
        return this.http
            .post<IQuestionhistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(questionhistory: IQuestionhistory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(questionhistory);
        return this.http
            .put<IQuestionhistory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IQuestionhistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IQuestionhistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IQuestionhistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(questionhistory: IQuestionhistory): IQuestionhistory {
        const copy: IQuestionhistory = Object.assign({}, questionhistory, {
            timestamp: questionhistory.timestamp != null && questionhistory.timestamp.isValid() ? questionhistory.timestamp.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.timestamp = res.body.timestamp != null ? moment(res.body.timestamp) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((questionhistory: IQuestionhistory) => {
            questionhistory.timestamp = questionhistory.timestamp != null ? moment(questionhistory.timestamp) : null;
        });
        return res;
    }
}
