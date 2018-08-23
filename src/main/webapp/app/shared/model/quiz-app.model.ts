import { IQuiz } from 'app/shared/model//quiz.model';
import { ICustomer } from 'app/shared/model//customer.model';

export interface IQuizApp {
    id?: number;
    option?: string;
    correct?: boolean;
    quiz?: IQuiz;
    customer?: ICustomer;
}

export class QuizApp implements IQuizApp {
    constructor(public id?: number, public option?: string, public correct?: boolean, public quiz?: IQuiz, public customer?: ICustomer) {
        this.correct = this.correct || false;
    }
}
