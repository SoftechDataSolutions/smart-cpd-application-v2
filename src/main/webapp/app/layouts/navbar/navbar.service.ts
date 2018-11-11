import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class NavbarService {
    public update: boolean;

    check() {
        this.update = true;
    }

    reset() {
        this.update = false;
    }

    flagReturn(): boolean {
        return this.update;
    }
}
