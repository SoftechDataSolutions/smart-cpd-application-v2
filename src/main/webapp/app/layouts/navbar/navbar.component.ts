import { ChangeDetectorRef, Component, DoCheck, EventEmitter, NgZone, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { VERSION } from 'app/app.constants';
import { JhiLanguageHelper, Principal, LoginModalService, LoginService, Account, IUser, UserService } from 'app/core';
import { ProfileService } from '../profiles/profile.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { ICart } from 'app/shared/model/cart.model';
import { ICourseCartBridge } from 'app/shared/model/course-cart-bridge.model';
import { CourseService } from 'app/entities/course';
import { CustomerService } from 'app/entities/customer';
import { CartService } from 'app/entities/cart';
import { CourseCartBridgeService } from 'app/entities/course-cart-bridge';
import { NavbarService } from 'app/layouts/navbar/navbar.service';
import setInterval = require('core-js/library/fn/set-interval');

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    currentAccount: Account;
    courseNumber = 0;
    user: IUser;
    userID = 0;
    customer: ICustomer;
    cart: ICart;
    bridgeCart: ICourseCartBridge[];
    tempCart: ICart;
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    flag = false;
    isCourse = false;
    isCheckout = false;
    instanceCnt = 0;
    flagTemp = false;

    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private router: Router,
        private principal: Principal,
        private courseService: CourseService,
        private customerService: CustomerService,
        private cartService: CartService,
        private courseCartService: CourseCartBridgeService,
        private userService: UserService,
        private navbarService: NavbarService,
        private ref: ChangeDetectorRef
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        this.bridgeCart = [];
    }

    /**ngDoCheck() {
        this.courseCartService.getcollection(this.cart.id).subscribe(bridges => {
            this.bridgeCart = bridges;
            if (this.bridgeCart.length !== this.courseNumber) {
                this.ngOnInit();
            }
        });
    }*/

    /**ngDoCheck() {
        this.courseCartService.getcollection(this.cart.id).subscribe(bridges => {
            this.bridgeCart = bridges;
            if (this.bridgeCart.length !== this.courseNumber) {
                this.ngOnInit();
            }
        });
    }*/

    ngOnInit() {
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });

        this.profileService.getProfileInfo().then(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
        setInterval(() => {
            this.isCourse = this.router.url.includes('/course', 0);
            this.isCheckout = this.router.url.includes('/checkout', 0);
            if (this.isCourse || this.isCheckout) {
                this.update();
            }
        }, 1000);
    }

    update() {
        this.flagTemp = this.navbarService.flagReturn();
        if (this.courseNumber === 0 && this.flagTemp) {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.userService.getemail(account.email).subscribe(users => {
                    this.userID = users;
                    this.customerService.getuser(this.userID).subscribe(customer => {
                        this.customer = customer;
                        this.cartService.check(this.customer.id).subscribe(carts => {
                            this.cart = carts;
                            this.tempCart = this.cart;
                            this.courseCartService.getcollection(this.cart.id).subscribe(bridges => {
                                this.bridgeCart = bridges;
                                if (this.cart.checkout) {
                                    this.courseNumber = 0;
                                } else {
                                    this.courseNumber = this.bridgeCart.length;
                                }
                            });
                        });
                    });
                });
            });
        } else if (this.flagTemp) {
            this.courseCartService.getcollection(this.cart.id).subscribe(bridges => {
                this.bridgeCart = bridges;
                if (this.cart.checkout) {
                    this.courseNumber = 0;
                } else {
                    this.courseNumber = this.bridgeCart.length;
                }
            });
        } else {
            this.navbarService.reset();
        }
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }
}
