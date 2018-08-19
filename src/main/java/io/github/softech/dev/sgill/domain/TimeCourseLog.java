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
 * A TimeCourseLog.
 */
@Entity
@Table(name = "time_course_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "timecourselog")
public class TimeCourseLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loggedin")
    private Instant loggedin;

    @Column(name = "loggedout")
    private Instant loggedout;

    @Column(name = "timespent")
    private Long timespent;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLoggedin() {
        return loggedin;
    }

    public TimeCourseLog loggedin(Instant loggedin) {
        this.loggedin = loggedin;
        return this;
    }

    public void setLoggedin(Instant loggedin) {
        this.loggedin = loggedin;
    }

    public Instant getLoggedout() {
        return loggedout;
    }

    public TimeCourseLog loggedout(Instant loggedout) {
        this.loggedout = loggedout;
        return this;
    }

    public void setLoggedout(Instant loggedout) {
        this.loggedout = loggedout;
    }

    public Long getTimespent() {
        return timespent;
    }

    public TimeCourseLog timespent(Long timespent) {
        this.timespent = timespent;
        return this;
    }

    public void setTimespent(Long timespent) {
        this.timespent = timespent;
    }

    public Customer getCustomer() {
        return customer;
    }

    public TimeCourseLog customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Course getCourse() {
        return course;
    }

    public TimeCourseLog course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
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
        TimeCourseLog timeCourseLog = (TimeCourseLog) o;
        if (timeCourseLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timeCourseLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimeCourseLog{" +
            "id=" + getId() +
            ", loggedin='" + getLoggedin() + "'" +
            ", loggedout='" + getLoggedout() + "'" +
            ", timespent=" + getTimespent() +
            "}";
    }
}
