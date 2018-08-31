export interface IQuiz {
    id?: number;
    name?: string;
    difficulty?: string;
    passingscore?: number;
}

export class Quiz implements IQuiz {
    constructor(public id?: number, public name?: string, public difficulty?: string, public passingscore?: number) {}
}
