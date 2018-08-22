import { IQuestion } from 'app/shared/model//question.model';

export interface IQuiz {
    id?: number;
    name?: string;
    difficulty?: string;
    passingscore?: number;
    questions?: IQuestion[];
}

export class Quiz implements IQuiz {
    constructor(
        public id?: number,
        public name?: string,
        public difficulty?: string,
        public passingscore?: number,
        public questions?: IQuestion[]
    ) {}
}
