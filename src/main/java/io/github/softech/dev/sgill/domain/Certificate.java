package io.github.softech.dev.sgill.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Certificate.
 */
@Entity
@Table(name = "certificate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "certificate")
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_timestamp")
    private Instant timestamp;

    @Lob
    @Column(name = "pdf")
    private byte[] pdf;

    @Column(name = "pdf_content_type")
    private String pdfContentType;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Course courses;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Certificate timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public Certificate pdf(byte[] pdf) {
        this.pdf = pdf;
        return this;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public String getPdfContentType() {
        return pdfContentType;
    }

    public Certificate pdfContentType(String pdfContentType) {
        this.pdfContentType = pdfContentType;
        return this;
    }

    public void setPdfContentType(String pdfContentType) {
        this.pdfContentType = pdfContentType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Certificate customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Course getCourses() {
        return courses;
    }

    public Certificate courses(Course course) {
        this.courses = course;
        return this;
    }

    public void setCourses(Course course) {
        this.courses = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Certificate certificate = (Certificate) o;
        if (certificate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), certificate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Certificate{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", pdf='" + getPdf() + "'" +
            ", pdfContentType='" + getPdfContentType() + "'" +
            "}";
    }
}
