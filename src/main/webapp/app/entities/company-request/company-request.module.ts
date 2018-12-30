import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartCpdSharedModule } from 'app/shared';
import {
    CompanyRequestComponent,
    CompanyRequestDetailComponent,
    CompanyRequestUpdateComponent,
    CompanyRequestDeletePopupComponent,
    CompanyRequestDeleteDialogComponent,
    companyRequestRoute,
    companyRequestPopupRoute
} from './';
import { FormsModule } from '@angular/forms';
import { AgmCoreModule } from '@agm/core';

const ENTITY_STATES = [...companyRequestRoute, ...companyRequestPopupRoute];

@NgModule({
    imports: [
        SmartCpdSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        FormsModule,
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyDLwKI-Tr33ZXmQ-b90c0OcmugQ4qkq3PA',
            libraries: ['places']
        })
    ],
    declarations: [
        CompanyRequestComponent,
        CompanyRequestDetailComponent,
        CompanyRequestUpdateComponent,
        CompanyRequestDeleteDialogComponent,
        CompanyRequestDeletePopupComponent
    ],
    entryComponents: [
        CompanyRequestComponent,
        CompanyRequestUpdateComponent,
        CompanyRequestDeleteDialogComponent,
        CompanyRequestDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdCompanyRequestModule {}
