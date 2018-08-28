import { IQuiz } from 'app/shared/model//quiz.model';
import { ICourse } from 'app/shared/model//course.model';
import { ITags } from 'app/shared/model//tags.model';

export interface ISection {
    id?: number;
    name?: string;
    notes?: string;
    normSection?: string;
    contentContentType?: string;
    content?: any;
    videoUrl?: string;
    textcontent?: any;
    type?: string;
    quiz?: IQuiz;
    course?: ICourse;
    tags?: ITags[];
}

export class Section implements ISection {
    constructor(
        public id?: number,
        public name?: string,
        public notes?: string,
        public normSection?: string,
        public contentContentType?: string,
        public content?: any,
        public videoUrl?: string,
        public textcontent?: any,
        public type?: string,
        public quiz?: IQuiz,
        public course?: ICourse,
        public tags?: ITags[]
    ) {}
}
