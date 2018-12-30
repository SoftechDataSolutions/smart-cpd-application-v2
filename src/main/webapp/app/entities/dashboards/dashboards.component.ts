import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { IDashboards } from 'app/shared/model/dashboards.model';
import { Account, Principal, UserService } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { DashboardsService } from './dashboards.service';
import { ICourseHistory } from 'app/shared/model/course-history.model';
import { ISectionHistory } from 'app/shared/model/section-history.model';
import { ICertificate } from 'app/shared/model/certificate.model';
import { ICourse } from 'app/shared/model/course.model';
import { ISection } from 'app/shared/model/section.model';
import { ICart } from 'app/shared/model/cart.model';
import { CustomerService } from 'app/entities/customer';
import { CartService } from 'app/entities/cart';
import { SectionHistoryService } from 'app/entities/section-history';
import { CertificateService } from 'app/entities/certificate';
import { CourseHistoryService } from 'app/entities/course-history';

@Component({
    selector: 'jhi-dashboards',
    templateUrl: './dashboards.component.html'
})
export class DashboardsComponent implements OnInit {
    dashboards: IDashboards[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;
    account: Account;
    custEmail: string;
    courses: ICourseHistory[];
    sections: ISectionHistory[];
    certificates: ICertificate[];
    recentCourse: ICourse;
    recentSection: ISection;
    carts: ICart[];

    constructor(
        private dashboardsService: DashboardsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private customerService: CustomerService,
        private userService: UserService,
        private cartService: CartService,
        private sectionHistoryService: SectionHistoryService,
        private certificateService: CertificateService,
        private courseHistoryService: CourseHistoryService
    ) {
        this.dashboards = [];
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

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.custEmail = this.currentAccount.email;
            this.userService.getemail(this.custEmail).subscribe(userId => {
                this.customerService.getuser(userId).subscribe(customer => {
                    this.cartService.getcustomer(customer.id).subscribe(cart => {
                        this.carts = cart;
                    });
                    this.sectionHistoryService.getcustomer(customer.id).subscribe(histData => {
                        this.sections = histData;
                    });
                    this.certificateService.getcustomer(customer.id).subscribe(certData => {
                        this.certificates = certData;
                    });
                    this.sectionHistoryService.getrecent(customer.id).subscribe(recentSec => {
                        this.recentSection = recentSec;
                    });
                    this.courseHistoryService.getrecent(customer.id).subscribe(lastCourse => {
                        this.recentCourse = lastCourse;
                    });
                });
            });
        });
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
