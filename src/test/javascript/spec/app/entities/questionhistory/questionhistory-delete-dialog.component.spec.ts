/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SmartCpdTestModule } from '../../../test.module';
import { QuestionhistoryDeleteDialogComponent } from 'app/entities/questionhistory/questionhistory-delete-dialog.component';
import { QuestionhistoryService } from 'app/entities/questionhistory/questionhistory.service';

describe('Component Tests', () => {
    describe('Questionhistory Management Delete Component', () => {
        let comp: QuestionhistoryDeleteDialogComponent;
        let fixture: ComponentFixture<QuestionhistoryDeleteDialogComponent>;
        let service: QuestionhistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartCpdTestModule],
                declarations: [QuestionhistoryDeleteDialogComponent]
            })
                .overrideTemplate(QuestionhistoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(QuestionhistoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QuestionhistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
