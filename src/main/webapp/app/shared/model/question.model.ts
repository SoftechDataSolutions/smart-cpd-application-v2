import { IChoice } from 'app/shared/model//choice.model';

export interface IQuestion {
    id?: number;
    textQuestion?: string;
    difficulty?: string;
    choices?: IChoice[];
}

export class Question implements IQuestion {
    constructor(public id?: number, public textQuestion?: string, public difficulty?: string, public choices?: IChoice[]) {}
}
