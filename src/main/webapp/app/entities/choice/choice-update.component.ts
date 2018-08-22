import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IChoice } from 'app/shared/model/choice.model';
import { ChoiceService } from './choice.service';

@Component({
    selector: 'jhi-choice-update',
    templateUrl: './choice-update.component.html'
})
export class ChoiceUpdateComponent implements OnInit {
    private _choice: IChoice;
    isSaving: boolean;

    constructor(private choiceService: ChoiceService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ choice }) => {
            this.choice = choice;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.choice.id !== undefined) {
            this.subscribeToSaveResponse(this.choiceService.update(this.choice));
        } else {
            this.subscribeToSaveResponse(this.choiceService.create(this.choice));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IChoice>>) {
        result.subscribe((res: HttpResponse<IChoice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get choice() {
        return this._choice;
    }

    set choice(choice: IChoice) {
        this._choice = choice;
    }
}
