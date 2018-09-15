import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ICourse } from 'app/shared/model/course.model';
import { Principal } from 'app/core';

import { DATE_TIME_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { CourseService } from './course.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { CartService } from 'app/entities/cart';
import { CourseCartBridgeService } from 'app/entities/course-cart-bridge';
import { ICart } from 'app/shared/model/cart.model';
import { CourseCartBridge, ICourseCartBridge } from 'app/shared/model/course-cart-bridge.model';
import * as moment from 'moment';

@Component({
    selector: 'jhi-course',
    templateUrl: './course.component.html'
})
export class CourseComponent implements OnInit, OnDestroy {
    courses: ICourse[];
    currentAccount: Account;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    userId: number;
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
    timestamp: string;

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
        private principal: Principal
    ) {
        this.courses = [];
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
            this.userId = Number(this.currentAccount.id);
            this.customerService.getbyuser(this.userId).subscribe(value => {
                this.customer = value.body;
            });
            this.cartService.check(this.customer.id).subscribe(value => {
                this.cart = value.body;
            });
            this.courseCartService.collection(this.cart.id).subscribe(value => {
                this.bridgeCart = value.body;
            });
            for (let i = 0; i < this.bridgeCart.length; i++) {
                this.coursesCart.push(this.bridgeCart[i].course);
            }
        });
        this.registerChangeInCourses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
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

    addToCart(course: ICourse) {
        this.coursesCart.push(course);
        this.newBridgeCart = new CourseCartBridge();
        this.newBridgeCart.course = course;
        this.newBridgeCart.cart = this.cart;
        this.newBridgeCart.timestamp = moment(this.timestamp, DATE_TIME_FORMAT);
        this.courseCartService.create(this.newBridgeCart);
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
