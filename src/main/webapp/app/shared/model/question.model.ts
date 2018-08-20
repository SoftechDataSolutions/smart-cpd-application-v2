import { IQuiz } from 'app/shared/model//quiz.model';

export interface IQuestion {
    id?: number;
    textQuestion?: string;
    difficulty?: string;
    quiz?: IQuiz;
}

export class Question implements IQuestion {
    constructor(public id?: number, public textQuestion?: string, public difficulty?: string, public quiz?: IQuiz) {}
}
