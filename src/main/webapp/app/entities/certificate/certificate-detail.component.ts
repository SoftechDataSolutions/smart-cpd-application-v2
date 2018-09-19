import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import * as jspdf from 'jspdf';
import * as html2canvas from 'html2canvas';
import { ICertificate } from 'app/shared/model/certificate.model';
import { CertificateService } from 'app/entities/certificate/certificate.service';
import { Account, Principal, IUser, UserService } from 'app/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import * as sgMail from '@sendgrid/mail';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared';
@Component({
    selector: 'jhi-certificate-detail',
    templateUrl: './certificate-detail.component.html'
})
export class CertificateDetailComponent implements OnInit {
    certificate: ICertificate;
    customer: ICustomer;
    user: IUser;
    pdf: jspdf;
    account: Account;
    userEmail: String;
    blob: Blob;
    arrayBuffer: ArrayBuffer;
    test: string;
    bufferLength: number;
    isSaving = false;
    bytes: any;
    timestamp: string;
    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private certificateService: CertificateService,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ certificate }) => {
            this.certificate = certificate;
        });
        this.principal.identity().then(account => {
            this.account = account;
            this.userEmail = this.account.email;
        });
        setTimeout(() => {
            this.convertToPDF();
        }, 3000);
        /**this.certificate.pdfContentType = 'data:application/pdf;base64';
         this.certificateService.update(this.certificate);*/
        this.customer = this.certificate.customer;
        this.user = this.certificate.customer.user;
    }

    convertToPDF() {
        const data = document.getElementById('convertPdf');
        html2canvas(data).then(canvas => {
            // Few necessary setting options
            const imgWidth = 300;
            const pageHeight = 400;
            const imgHeight = canvas.height * imgWidth / canvas.width;
            const heightLeft = imgHeight;
            const reader = new FileReader();
            const contentDataURL = canvas.toDataURL('image/png');
            this.pdf = new jspdf('l', 'mm', 'a4', 1);
            this.pdf.addImage(contentDataURL, 'PNG', 0, 0, imgWidth, imgHeight, '', 'FAST');
            this.pdf.save('certificate.pdf');
            this.blob = this.pdf.output('blob');
            this.arrayBuffer = this.pdf.output('arraybuffer');
            /**this.email();*/
            /**this.certificateService.email(this.certificate.id);*/
        });
    }

    email() {
        this.certificateService.email(this.certificate.id);
        /**this.certificateService.attachment(this.bufferToBase64(new Uint8Array(this.arrayBuffer)), this.certificate.id);*/
    }

    bufferToBase64(buffer: Uint8Array) {
        const binstr = Array.prototype.map
            .call(buffer, function(ch) {
                return String.fromCharCode(ch);
            })
            .join('');
        return btoa(binstr);
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    previousState() {
        window.history.back();
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICertificate>>) {
        result.subscribe((res: HttpResponse<ICertificate>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
