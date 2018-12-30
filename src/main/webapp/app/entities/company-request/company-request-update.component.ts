import { Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICompanyRequest } from 'app/shared/model/company-request.model';
import { CompanyRequestService } from './company-request.service';
import { MapsAPILoader } from '@agm/core';

@Component({
    selector: 'jhi-company-request-update',
    templateUrl: './company-request-update.component.html'
})
export class CompanyRequestUpdateComponent implements OnInit {
    private _companyRequest: ICompanyRequest;
    isSaving: boolean;
    cycledate: string;
    public addr: string;
    public city: string;
    public country: string;
    public state: string;
    @ViewChild('search') public searchElement: ElementRef;
    constructor(
        private companyRequestService: CompanyRequestService,
        private activatedRoute: ActivatedRoute,
        private mapsAPILoader: MapsAPILoader,
        private ngZone: NgZone
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ companyRequest }) => {
            this.companyRequest = companyRequest;
        });
        this.mapsAPILoader.load().then(() => {
            const autocomplete = new google.maps.places.Autocomplete(this.searchElement.nativeElement, { types: ['address'] });
            autocomplete.addListener('place_changed', () => {
                this.ngZone.run(() => {
                    const place: google.maps.places.PlaceResult = autocomplete.getPlace();
                    if (place.geometry === undefined || place.geometry === null) {
                        return;
                    }
                    for (let i = 0; i < place.address_components.length; i++) {
                        const addressType = place.address_components[i].types[0];
                        switch (addressType) {
                            case 'street_number':
                                this.addr = place.address_components[i].long_name;
                                break;
                            case 'route':
                                this.addr += ' ' + place.address_components[i].long_name;
                                break;
                            case 'locality':
                                this.city = place.address_components[i].long_name;
                                break;
                            case 'administrative_area_level_1':
                                this.state = place.address_components[i].long_name;
                                break;
                            case 'country':
                                this.country = place.address_components[i].short_name;
                                break;
                        }
                    }
                });
            });
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.companyRequest.streetAddress = this.addr;
        this.companyRequest.city = this.city;
        this.companyRequest.country = this.country;
        this.companyRequest.stateProvince = this.state;
        this.companyRequest.cycledate = moment(this.cycledate, DATE_TIME_FORMAT);
        if (this.companyRequest.id !== undefined) {
            this.subscribeToSaveResponse(this.companyRequestService.update(this.companyRequest));
        } else {
            this.subscribeToSaveResponse(this.companyRequestService.create(this.companyRequest));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyRequest>>) {
        result.subscribe((res: HttpResponse<ICompanyRequest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get companyRequest() {
        return this._companyRequest;
    }

    set companyRequest(companyRequest: ICompanyRequest) {
        this._companyRequest = companyRequest;
        this.cycledate = moment(companyRequest.cycledate).format(DATE_TIME_FORMAT);
    }
}
