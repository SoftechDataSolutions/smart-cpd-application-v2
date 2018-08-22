export interface IChoice {
    id?: number;
    textChoice?: string;
    isanswer?: boolean;
}

export class Choice implements IChoice {
    constructor(public id?: number, public textChoice?: string, public isanswer?: boolean) {
        this.isanswer = this.isanswer || false;
    }
}
