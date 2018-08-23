import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model//customer.model';
import { IQuestion } from 'app/shared/model//question.model';
import { IChoice } from 'app/shared/model//choice.model';

export interface IQuestionHistory {
    id?: number;
    timestamp?: Moment;
    correct?: boolean;
    customer?: ICustomer;
    question?: IQuestion;
    choice?: IChoice;
}

export class QuestionHistory implements IQuestionHistory {
    constructor(
        public id?: number,
        public timestamp?: Moment,
        public correct?: boolean,
        public customer?: ICustomer,
        public question?: IQuestion,
        public choice?: IChoice
    ) {
        this.correct = this.correct || false;
    }
}
