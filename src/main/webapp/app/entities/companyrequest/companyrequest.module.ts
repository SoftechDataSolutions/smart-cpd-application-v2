import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartCpdSharedModule } from 'app/shared';
import { CompanyrequestComponent, companyrequestRoute } from './';

const ENTITY_STATES = [...companyrequestRoute];

@NgModule({
    imports: [SmartCpdSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CompanyrequestComponent],
    entryComponents: [CompanyrequestComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdCompanyrequestModule {}
