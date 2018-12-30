import { Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from './company.service';
import { MapsAPILoader } from '@agm/core';

@Component({
    selector: 'jhi-company-update',
    templateUrl: './company-update.component.html'
})
export class CompanyUpdateComponent implements OnInit {
    private _company: ICompany;
    isSaving: boolean;
    cycledate: string;
    public addr: string;
    public city: string;
    public country: string;
    public state: string;
    @ViewChild('search') public searchElement: ElementRef;
    constructor(
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute,
        private mapsAPILoader: MapsAPILoader,
        private ngZone: NgZone
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ company }) => {
            this.company = company;
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
        this.company.streetAddress = this.addr;
        this.company.city = this.city;
        this.company.country = this.country;
        this.company.stateProvince = this.state;
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
        return this._company;
    }

    set company(company: ICompany) {
        this._company = company;
        this.cycledate = moment(company.cycledate).format(DATE_TIME_FORMAT);
    }
}
