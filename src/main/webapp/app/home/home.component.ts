import { AfterViewChecked, Component, NgZone, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, Principal, Account, UserService } from 'app/core';
import { CustomerService } from 'app/entities/customer';
import { ICustomer } from 'app/shared/model/customer.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    custEmail: string;
    customer: ICustomer;
    name: string;
    init = false;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private userService: UserService,
        private customerService: CustomerService,
        private zone: NgZone
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            this.custEmail = account.email;
            this.userService.getemail(this.custEmail).subscribe(userId => {
                this.customerService.getuser(userId).subscribe(customer => {
                    this.customer = customer;
                    this.name = customer.normalized;
                });
            });
        });
        this.registerAuthenticationSuccess();
    }

    /** ngAfterViewChecked() {
        location.reload();
    }*/

    public reloadPage() {
        location.reload();
    }

    setInit() {
        this.init = true;
    }

    checkReload() {
        if (this.init) {
            location.reload(true);
        }
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
