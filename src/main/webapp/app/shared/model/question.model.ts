import { IChoice } from 'app/shared/model//choice.model';
import { IQuiz } from 'app/shared/model//quiz.model';

export interface IQuestion {
    id?: number;
    textQuestion?: string;
    difficulty?: string;
    choices?: IChoice[];
    quiz?: IQuiz;
}

export class Question implements IQuestion {
    constructor(
        public id?: number,
        public textQuestion?: string,
        public difficulty?: string,
        public choices?: IChoice[],
        public quiz?: IQuiz
    ) {}
}
