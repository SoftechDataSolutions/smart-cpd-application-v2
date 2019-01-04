import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartCpdSharedModule } from 'app/shared';
import { CheckoutComponent, checkoutRoute } from './';

const ENTITY_STATES = [...checkoutRoute];

@NgModule({
    imports: [SmartCpdSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CheckoutComponent],
    entryComponents: [CheckoutComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdCheckoutModule {}
