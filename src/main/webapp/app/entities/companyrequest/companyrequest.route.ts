import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Companyrequest } from 'app/shared/model/companyrequest.model';
import { CompanyrequestService } from './companyrequest.service';
import { CompanyrequestComponent } from './companyrequest.component';

export const companyrequestRoute: Routes = [
    {
        path: 'companyrequest',
        component: CompanyrequestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartCpdApp.companyrequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
