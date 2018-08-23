/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SmartCpdTestModule } from '../../../test.module';
import { QuestionhistoryUpdateComponent } from 'app/entities/questionhistory/questionhistory-update.component';
import { QuestionhistoryService } from 'app/entities/questionhistory/questionhistory.service';
import { Questionhistory } from 'app/shared/model/questionhistory.model';

describe('Component Tests', () => {
    describe('Questionhistory Management Update Component', () => {
        let comp: QuestionhistoryUpdateComponent;
        let fixture: ComponentFixture<QuestionhistoryUpdateComponent>;
        let service: QuestionhistoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartCpdTestModule],
                declarations: [QuestionhistoryUpdateComponent]
            })
                .overrideTemplate(QuestionhistoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(QuestionhistoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QuestionhistoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Questionhistory(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.questionhistory = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Questionhistory();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.questionhistory = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
