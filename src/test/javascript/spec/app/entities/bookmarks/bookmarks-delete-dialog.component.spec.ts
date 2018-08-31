/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SmartCpdTestModule } from '../../../test.module';
import { BookmarksDeleteDialogComponent } from 'app/entities/bookmarks/bookmarks-delete-dialog.component';
import { BookmarksService } from 'app/entities/bookmarks/bookmarks.service';

describe('Component Tests', () => {
    describe('Bookmarks Management Delete Component', () => {
        let comp: BookmarksDeleteDialogComponent;
        let fixture: ComponentFixture<BookmarksDeleteDialogComponent>;
        let service: BookmarksService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartCpdTestModule],
                declarations: [BookmarksDeleteDialogComponent]
            })
                .overrideTemplate(BookmarksDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BookmarksDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookmarksService);
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
