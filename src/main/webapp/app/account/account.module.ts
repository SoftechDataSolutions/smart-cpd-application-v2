import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgmCoreModule } from '@agm/core';
import { SmartCpdSharedModule } from 'app/shared';
import { FormsModule } from '@angular/forms';

import {
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    accountState
} from './';
import { GooglePlacesDirective } from './register/google-places.directive';

@NgModule({
    imports: [
        SmartCpdSharedModule,
        RouterModule.forChild(accountState),
        FormsModule,
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyDLwKI-Tr33ZXmQ-b90c0OcmugQ4qkq3PA',
            libraries: ['places']
        })
    ],
    declarations: [
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdAccountModule {}
