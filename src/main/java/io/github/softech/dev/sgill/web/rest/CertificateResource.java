package io.github.softech.dev.sgill.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.softech.dev.sgill.domain.Certificate;
import io.github.softech.dev.sgill.service.CertificateService;
import io.github.softech.dev.sgill.service.MailService;
import io.github.softech.dev.sgill.web.rest.errors.BadRequestAlertException;
import io.github.softech.dev.sgill.web.rest.util.HeaderUtil;
import io.github.softech.dev.sgill.web.rest.util.PaginationUtil;
import io.github.softech.dev.sgill.service.dto.CertificateCriteria;
import io.github.softech.dev.sgill.service.CertificateQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.sendgrid.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Certificate.
 */
@RestController
@RequestMapping("/api")
public class CertificateResource {

    private final Logger log = LoggerFactory.getLogger(CertificateResource.class);

    private static final String ENTITY_NAME = "certificate";

    private final CertificateService certificateService;

    private final CertificateQueryService certificateQueryService;

    private final MailService mailService;

    public CertificateResource(CertificateService certificateService, CertificateQueryService certificateQueryService
        , MailService mailService) {
        this.certificateService = certificateService;
        this.certificateQueryService = certificateQueryService;
        this.mailService = mailService;
    }

    /**
     * POST  /certificates : Create a new certificate.
     *
     * @param certificate the certificate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new certificate, or with status 400 (Bad Request) if the certificate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/certificates")
    @Timed
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate certificate) throws URISyntaxException {
        log.debug("REST request to save Certificate : {}", certificate);
        if (certificate.getId() != null) {
            throw new BadRequestAlertException("A new certificate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Certificate result = certificateService.save(certificate);
        return ResponseEntity.created(new URI("/api/certificates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /certificates : Updates an existing certificate.
     *
     * @param certificate the certificate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated certificate,
     * or with status 400 (Bad Request) if the certificate is not valid,
     * or with status 500 (Internal Server Error) if the certificate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/certificates")
    @Timed
    public ResponseEntity<Certificate> updateCertificate(@RequestBody Certificate certificate) throws URISyntaxException {
        log.debug("REST request to update Certificate : {}", certificate);
        if (certificate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Certificate result = certificateService.save(certificate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, certificate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /certificates : get all the certificates.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of certificates in body
     */
    @GetMapping("/certificates")
    @Timed
    public ResponseEntity<List<Certificate>> getAllCertificates(CertificateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Certificates by criteria: {}", criteria);
        Page<Certificate> page = certificateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/certificates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /certificates/:id : get the "id" certificate.
     *
     * @param id the id of the certificate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the certificate, or with status 404 (Not Found)
     */
    @GetMapping("/certificates/{id}")
    @Timed
    public ResponseEntity<Certificate> getCertificate(@PathVariable Long id) {
        log.debug("REST request to get Certificate : {}", id);
        Optional<Certificate> certificate = certificateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(certificate);
    }

    /**
     * DELETE  /certificates/:id : delete the "id" certificate.
     *
     * @param id the id of the certificate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/certificates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        log.debug("REST request to delete Certificate : {}", id);
        certificateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/certificates?query=:query : search for the certificate corresponding
     * to the query.
     *
     * @param query the query of the certificate search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/certificates")
    @Timed
    public ResponseEntity<List<Certificate>> searchCertificates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Certificates for query {}", query);
        Page<Certificate> page = certificateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/certificates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/_email/certificates/{id}")
    @Timed
    public ResponseEntity<Certificate> sendCertificates(@PathVariable Long id, @Valid @RequestParam String file) throws URISyntaxException, IOException {
        log.debug("SEND REST Request {} ID is" + id.toString());
        log.debug("File Length" + file.length());
        Certificate certificate = certificateService.findOne(id).orElse(null);
        //mailService.sendCertificateEmail("gill.shaanjot@gmail.com", pdf, true);
        Email from = new Email("gill.shaanjot@gmail.com");
        //String subject = "Your Certificate for Course" + certificate.getCourses().getNormCourses();
        //Email to = new Email(certificate.getCustomer().getUser().getEmail());
        String subject = "Your Certificate for Course Roofs 101";
        Email to = new Email("gill.shaanjots@gmail.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Attachments attachment = new Attachments();
        attachment.setContent(file);
        attachment.setFilename("certificate.pdf");
        attachment.setType("application/pdf");
        attachment.setDisposition("attachment");
        Mail mail = new Mail(from, subject, to, content);
        mail.addAttachments(attachment);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());

        byte[] pdf = file.getBytes();
        certificate.setPdf(pdf);
        certificate.setPdfContentType("application/pdf");
        Certificate result = certificateService.save(certificate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, certificate.getId().toString()))
            .body(result);
    }
}
