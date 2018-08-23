/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SmartCpdTestModule } from '../../../test.module';
import { QuestionhistoryDetailComponent } from 'app/entities/questionhistory/questionhistory-detail.component';
import { Questionhistory } from 'app/shared/model/questionhistory.model';

describe('Component Tests', () => {
    describe('Questionhistory Management Detail Component', () => {
        let comp: QuestionhistoryDetailComponent;
        let fixture: ComponentFixture<QuestionhistoryDetailComponent>;
        const route = ({ data: of({ questionhistory: new Questionhistory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartCpdTestModule],
                declarations: [QuestionhistoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(QuestionhistoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(QuestionhistoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.questionhistory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
