import { IQuiz } from 'app/shared/model//quiz.model';
import { ICourse } from 'app/shared/model//course.model';

export interface ISection {
    id?: number;
    name?: string;
    notes?: string;
    normSection?: string;
    contentContentType?: string;
    content?: any;
    textContent?: string;
    videoUrl?: string;
    quiz?: IQuiz;
    course?: ICourse;
}

export class Section implements ISection {
    constructor(
        public id?: number,
        public name?: string,
        public notes?: string,
        public normSection?: string,
        public contentContentType?: string,
        public content?: any,
        public textContent?: string,
        public videoUrl?: string,
        public quiz?: IQuiz,
        public course?: ICourse
    ) {}
}
