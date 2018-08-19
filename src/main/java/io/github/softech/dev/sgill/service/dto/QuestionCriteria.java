package io.github.softech.dev.sgill.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Question entity. This class is used in QuestionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /questions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter textQuestion;

    private StringFilter difficulty;

    private LongFilter quizId;

    public QuestionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(StringFilter textQuestion) {
        this.textQuestion = textQuestion;
    }

    public StringFilter getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(StringFilter difficulty) {
        this.difficulty = difficulty;
    }

    public LongFilter getQuizId() {
        return quizId;
    }

    public void setQuizId(LongFilter quizId) {
        this.quizId = quizId;
    }

    @Override
    public String toString() {
        return "QuestionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (textQuestion != null ? "textQuestion=" + textQuestion + ", " : "") +
                (difficulty != null ? "difficulty=" + difficulty + ", " : "") +
                (quizId != null ? "quizId=" + quizId + ", " : "") +
            "}";
    }

}
