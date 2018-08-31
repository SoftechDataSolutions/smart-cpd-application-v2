import { ISection } from 'app/shared/model//section.model';

export interface IBookmarks {
    id?: number;
    bookmark?: string;
    text?: string;
    section?: ISection;
}

export class Bookmarks implements IBookmarks {
    constructor(public id?: number, public bookmark?: string, public text?: string, public section?: ISection) {}
}
