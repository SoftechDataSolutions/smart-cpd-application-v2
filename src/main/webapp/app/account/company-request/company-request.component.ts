import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-company-request',
    templateUrl: './company-request.component.html'
})
export class CompanyRequestComponent implements OnInit {
    private _companyrequest: ICompany;
    isSaving: boolean;
    cycledate: string;

    constructor(private companyService: CompanyService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ company }) => {
            this.company = company;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.company.cycledate = moment(this.cycledate, DATE_TIME_FORMAT);
        if (this.company.id !== undefined) {
            this.subscribeToSaveResponse(this.companyService.update(this.company));
        } else {
            this.subscribeToSaveResponse(this.companyService.create(this.company));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>) {
        result.subscribe((res: HttpResponse<ICompany>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get company() {
        return this._companyrequest;
    }

    set company(company: ICompany) {
        this._companyrequest = company;
        this.cycledate = moment(company.cycledate).format(DATE_TIME_FORMAT);
    }
}
