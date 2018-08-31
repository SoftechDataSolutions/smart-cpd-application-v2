import { Component, OnInit, AfterViewInit, Renderer, ElementRef, ViewChild, NgZone } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {} from '@types/googlemaps';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared';
import { LoginModalService } from 'app/core';
import { Register } from './register.service';
import { CustomerService } from 'app/entities/customer';
import { ICustomer } from 'app/shared/model/customer.model';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';
import { MapsAPILoader } from '@agm/core';
import moment = require('moment');

@Component({
    selector: 'jhi-register',
    templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit, AfterViewInit {
    confirmPassword: string;
    doNotMatch: string;
    error: string;
    errorEmailExists: string;
    errorUserExists: string;
    registerAccount: any;
    success: boolean;
    modalRef: NgbModalRef;
    companies: ICompany[];
    customer: ICustomer;
    public addr: string;
    public city: string;
    public country: string;
    public state: string;
    public date: string;
    @ViewChild('search') public searchElement: ElementRef;
    constructor(
        private languageService: JhiLanguageService,
        private loginModalService: LoginModalService,
        private registerService: Register,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private mapsAPILoader: MapsAPILoader,
        private ngZone: NgZone
    ) {}

    ngOnInit() {
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
        this.success = false;
        this.registerAccount = {};
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.date = this.registerAccount.company.cycledate;
    }

    selectCompany(company: ICompany) {
        this.date = company.cycledate.toString();
    }
    /*
    setAddress(addrObj) {
        this.zone.run(() => {
            this.addr = addrObj;
            this.addrKeys = Object.keys(addrObj);
        });
    }*/
    /*
    public handleAddressChange(address: Address) {
        const array = address.address_components;
        this.addr = this.extractFromAdress(array, 'street_address');
        this.country = this.extractFromAdress(array, 'country');
        this.state = this.extractFromAdress(array, 'administrative_area_level_1');
        this.city = this.extractFromAdress(array, 'locality');
        this.postCode = this.extractFromAdress(array, 'postal_code');
    }*/

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    extractFromAdress(components: any, type: string) {
        for (let i = 0; i < components.length; i++) {
            for (let j = 0; j < components[i].types.length; j++) {
                if (components[i].types[j] === type) {
                    return components[i].long_name;
                }
            }
            return '';
        }
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
    }

    register() {
        this.registerAccount.streetaddress = this.addr + ', ' + this.registerAccount.apt;
        this.registerAccount.city = this.city;
        this.registerAccount.country = this.country;
        this.registerAccount.stateProvince = this.state;
        if (this.registerAccount.password !== this.confirmPassword) {
            this.doNotMatch = 'ERROR';
        } else {
            this.doNotMatch = null;
            this.error = null;
            this.errorUserExists = null;
            this.errorEmailExists = null;
            this.languageService.getCurrent().then(key => {
                this.registerAccount.langKey = key;
                this.registerService.save(this.registerAccount).subscribe(
                    () => {
                        this.success = true;
                    },
                    response => this.processError(response)
                );
            });
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    private onSaveSuccess() {
        this.success = false;
        this.previousState();
    }

    private onSaveError() {
        this.success = false;
    }

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }

    openLogin() {
        this.modalRef = this.loginModalService.open();
    }

    private processError(response: HttpErrorResponse) {
        this.success = null;
        if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
            this.errorUserExists = 'ERROR';
        } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
            this.errorEmailExists = 'ERROR';
        } else {
            this.error = 'ERROR';
        }
    }
}
