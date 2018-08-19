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
 * A QuizHistory.
 */
@Entity
@Table(name = "quiz_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "quizhistory")
public class QuizHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_start")
    private Instant start;

    @Column(name = "passed")
    private Boolean passed;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Quiz quiz;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStart() {
        return start;
    }

    public QuizHistory start(Instant start) {
        this.start = start;
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Boolean isPassed() {
        return passed;
    }

    public QuizHistory passed(Boolean passed) {
        this.passed = passed;
        return this;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public Customer getCustomer() {
        return customer;
    }

    public QuizHistory customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public QuizHistory quiz(Quiz quiz) {
        this.quiz = quiz;
        return this;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
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
        QuizHistory quizHistory = (QuizHistory) o;
        if (quizHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quizHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuizHistory{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", passed='" + isPassed() + "'" +
            "}";
    }
}
