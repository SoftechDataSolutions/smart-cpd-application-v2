import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { ICourse } from 'app/shared/model/course.model';
import { Principal, UserService, IUser, Account } from 'app/core';

import { DATE_TIME_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { CourseService } from './course.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { CartService } from 'app/entities/cart';
import { courseCartBridgePopupRoute, CourseCartBridgeService } from 'app/entities/course-cart-bridge';
import { ICart } from 'app/shared/model/cart.model';
import { CourseCartBridge, ICourseCartBridge } from 'app/shared/model/course-cart-bridge.model';
import * as moment from 'moment';
import { JhiTrackerService } from 'app/core';
import { IamUserArn } from 'aws-sdk/clients/codedeploy';
import { __values } from 'tslib';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { updateSourceFile } from '@angular/compiler-cli/src/transformers/node_emitter';

@Component({
    selector: 'jhi-course',
    templateUrl: './course.component.html'
})
export class CourseComponent implements OnInit, OnDestroy {
    courses: ICourse[];
    /**reqdAccount: Account;*/
    currentAccount: Account;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;
    customer: ICustomer;
    cart: ICart;
    coursesCart: ICourse[];
    bridgeCart: ICourseCartBridge[];
    newBridgeCart: ICourseCartBridge;
    courseNumber: number;
    timestamp: string;
    user: IUser;
    userID = 0;
    message: string;
    isSaving: boolean;
    testNull: boolean;
    testCartCourses: boolean;
    initAmount = 0;

    constructor(
        private courseService: CourseService,
        private customerService: CustomerService,
        private cartService: CartService,
        private courseCartService: CourseCartBridgeService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private trackService: JhiTrackerService,
        private principal: Principal,
        private http: HttpClient
    ) {
        this.courses = [];
        this.bridgeCart = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.courseService
                .search({
                    query: this.currentSearch,
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<ICourse[]>) => this.paginateCourses(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.courseService
            .query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<ICourse[]>) => this.paginateCourses(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    reset() {
        this.page = 0;
        this.courses = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.courses = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.courses = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.userService.getemail(account.email).subscribe(users => {
                this.userID = users;
                this.customerService.getuser(this.userID).subscribe(customer => {
                    this.customer = customer;
                    this.cartService.check(this.customer.id).subscribe(carts => {
                        this.cart = carts;
                        this.courseCartService.getcollection(this.cart.id).subscribe(bridges => {
                            this.bridgeCart = bridges;
                        });
                    });
                });
            });
        });
    }

    ngOnDestroy() {
        /*this.eventSubscriber.unsubscribe();*/
    }

    trackId(index: number, item: ICourse) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    public addCourse(course: ICourse) {
        if (this.findinCart(course)) {
            this.message = 'This course is already added to your cart';
        } else {
            this.newBridgeCart = new CourseCartBridge();
            this.newBridgeCart.course = course;
            this.newBridgeCart.cart = this.cart;
            this.newBridgeCart.timestamp = moment();
            if (this.bridgeCart == null) {
                this.bridgeCart[0] = this.newBridgeCart;
                this.cart.amount += course.amount;
                this.save(this.newBridgeCart);
            } else {
                this.bridgeCart[this.bridgeCart.length] = this.newBridgeCart;
                this.cart.amount += course.amount;
                this.save(this.newBridgeCart);
            }
            this.courseNumber = this.bridgeCart.length;
            this.updateCartAmount(this.cart, course.amount);
        }
    }

    updateCartAmount(temp: ICart, cost: number, point: number) {
        this.isSaving = true;
        if (temp.id !== undefined) {
            this.initAmount = temp.amount;
            temp.amount = this.initAmount + cost;
            temp.this.subscribeToSaveCartResponse(this.cartService.update(temp));
        } else {
            this.subscribeToSaveCartResponse(this.cartService.create(temp));
        }
    }

    save(temp: ICourseCartBridge) {
        this.isSaving = true;
        if (temp.id !== undefined) {
            this.subscribeToSaveResponse(this.courseCartService.update(temp));
        } else {
            this.subscribeToSaveResponse(this.courseCartService.create(temp));
        }
    }

    private subscribeToSaveCartResponse(result: Observable<HttpResponse<ICart>>) {
        result.subscribe((res: HttpResponse<ICart>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICourseCartBridge>>) {
        result.subscribe((res: HttpResponse<ICourseCartBridge>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    findinCart(course: ICourse) {
        if (this.bridgeCart.length === 0) {
            this.testNull = true;
            return false;
        } else {
            for (let i = 0; i < this.bridgeCart.length; i++) {
                if (this.bridgeCart[i].course.id === course.id) {
                    this.testCartCourses = true;
                    return true;
                }
            }
        }
        this.testCartCourses = false;
        return false;
    }

    registerChangeInCourses() {
        this.eventSubscriber = this.eventManager.subscribe('courseListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCourses(data: ICourse[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.courses.push(data[i]);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
