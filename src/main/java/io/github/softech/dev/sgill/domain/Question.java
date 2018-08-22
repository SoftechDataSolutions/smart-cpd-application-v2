package io.github.softech.dev.sgill.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "text_question", nullable = false)
    private String textQuestion;

    @Column(name = "difficulty")
    private String difficulty;

    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Choice> choices = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("questions")
    private Quiz quiz;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextQuestion() {
        return textQuestion;
    }

    public Question textQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
        return this;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Question difficulty(String difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Set<Choice> getChoices() {
        return choices;
    }

    public Question choices(Set<Choice> choices) {
        this.choices = choices;
        return this;
    }

    public Question addChoice(Choice choice) {
        this.choices.add(choice);
        choice.setQuestion(this);
        return this;
    }

    public Question removeChoice(Choice choice) {
        this.choices.remove(choice);
        choice.setQuestion(null);
        return this;
    }

    public void setChoices(Set<Choice> choices) {
        this.choices = choices;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Question quiz(Quiz quiz) {
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
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", textQuestion='" + getTextQuestion() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            "}";
    }
}
