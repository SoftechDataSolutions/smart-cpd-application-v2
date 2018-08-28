import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

// import needed PrimeNG modules here

import { SmartCpdSharedModule } from '../../../shared';
import { TabViewModule } from 'primeng/components/tabview/tabview';
import { GrowlModule } from 'primeng/components/growl/growl';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { TabViewDemoComponent, tabviewDemoRoute } from './';

const primeng_STATES = [tabviewDemoRoute];

@NgModule({
    imports: [
        SmartCpdSharedModule,
        BrowserModule,
        FormsModule,
        TabViewModule,
        GrowlModule,
        WizardModule,
        RouterModule.forRoot(primeng_STATES, { useHash: true })
    ],
    declarations: [TabViewDemoComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdTabViewDemoModule {}
