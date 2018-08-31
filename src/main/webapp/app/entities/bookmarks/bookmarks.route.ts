import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Bookmarks } from 'app/shared/model/bookmarks.model';
import { BookmarksService } from './bookmarks.service';
import { BookmarksComponent } from './bookmarks.component';
import { BookmarksDetailComponent } from './bookmarks-detail.component';
import { BookmarksUpdateComponent } from './bookmarks-update.component';
import { BookmarksDeletePopupComponent } from './bookmarks-delete-dialog.component';
import { IBookmarks } from 'app/shared/model/bookmarks.model';

@Injectable({ providedIn: 'root' })
export class BookmarksResolve implements Resolve<IBookmarks> {
    constructor(private service: BookmarksService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((bookmarks: HttpResponse<Bookmarks>) => bookmarks.body));
        }
        return of(new Bookmarks());
    }
}

export const bookmarksRoute: Routes = [
    {
        path: 'bookmarks',
        component: BookmarksComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'smartCpdApp.bookmarks.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bookmarks/:id/view',
        component: BookmarksDetailComponent,
        resolve: {
            bookmarks: BookmarksResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.bookmarks.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bookmarks/new',
        component: BookmarksUpdateComponent,
        resolve: {
            bookmarks: BookmarksResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.bookmarks.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'bookmarks/:id/edit',
        component: BookmarksUpdateComponent,
        resolve: {
            bookmarks: BookmarksResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.bookmarks.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookmarksPopupRoute: Routes = [
    {
        path: 'bookmarks/:id/delete',
        component: BookmarksDeletePopupComponent,
        resolve: {
            bookmarks: BookmarksResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.bookmarks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
