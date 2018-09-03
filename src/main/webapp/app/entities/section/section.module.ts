import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { EditorModule } from 'primeng/components/editor/editor';
import { ButtonModule } from 'primeng/components/button/button';
import { GrowlModule } from 'primeng/components/growl/growl';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';
import { SharedModule } from 'primeng/components/common/shared';
import { SmartCpdSharedModule } from 'app/shared';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import {
    SectionComponent,
    SectionDetailComponent,
    SectionUpdateComponent,
    SectionDeletePopupComponent,
    SectionDeleteDialogComponent,
    sectionRoute,
    sectionPopupRoute
} from './';

const ENTITY_STATES = [...sectionRoute, ...sectionPopupRoute];

@NgModule({
    imports: [
        SmartCpdSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        FormsModule,
        EditorModule,
        ButtonModule,
        GrowlModule,
        WizardModule,
        SharedModule,
        PdfViewerModule
    ],
    declarations: [
        SectionComponent,
        SectionDetailComponent,
        SectionUpdateComponent,
        SectionDeleteDialogComponent,
        SectionDeletePopupComponent
    ],
    entryComponents: [SectionComponent, SectionUpdateComponent, SectionDeleteDialogComponent, SectionDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdSectionModule {}
