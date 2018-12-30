import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SmartCpdSharedModule } from 'app/shared';
import { CheckoutComponent, checkoutRoute } from './';
import { NgxStripeModule } from 'ngx-stripe';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

const ENTITY_STATES = [...checkoutRoute];

@NgModule({
    imports: [
        SmartCpdSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        NgxStripeModule.forRoot('pk_test_FTFwNEdT7eeQVeZos4vyasZJ'),
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule
    ],
    declarations: [CheckoutComponent],
    entryComponents: [CheckoutComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdCheckoutModule {}
