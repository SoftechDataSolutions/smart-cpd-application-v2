import { IQuiz } from 'app/shared/model//quiz.model';
import { ICustomer } from 'app/shared/model//customer.model';
import { ISection } from 'app/shared/model//section.model';
import { IQuestion } from 'app/shared/model//question.model';

export interface IQuizApp {
    id?: number;
    quiz?: IQuiz;
    customer?: ICustomer;
    currSection?: ISection;
    newSection?: ISection;
    questions?: IQuestion[];
}

export class QuizApp implements IQuizApp {
    constructor(
        public id?: number,
        public quiz?: IQuiz,
        public customer?: ICustomer,
        public currSection?: ISection,
        public newSection?: ISection,
        public questions?: IQuestion[]
    ) {}
}
