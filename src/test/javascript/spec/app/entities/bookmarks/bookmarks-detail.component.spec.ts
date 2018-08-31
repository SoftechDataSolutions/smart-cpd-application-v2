/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SmartCpdTestModule } from '../../../test.module';
import { BookmarksDetailComponent } from 'app/entities/bookmarks/bookmarks-detail.component';
import { Bookmarks } from 'app/shared/model/bookmarks.model';

describe('Component Tests', () => {
    describe('Bookmarks Management Detail Component', () => {
        let comp: BookmarksDetailComponent;
        let fixture: ComponentFixture<BookmarksDetailComponent>;
        const route = ({ data: of({ bookmarks: new Bookmarks(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartCpdTestModule],
                declarations: [BookmarksDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BookmarksDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BookmarksDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.bookmarks).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
