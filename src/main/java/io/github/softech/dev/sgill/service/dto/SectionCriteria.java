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
 * Criteria class for the Section entity. This class is used in SectionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sections?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SectionCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter notes;

    private StringFilter normSection;

    private StringFilter videoUrl;

    private StringFilter type;

    private LongFilter quizId;

    private LongFilter courseId;

    private LongFilter tagsId;

    public SectionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public StringFilter getNormSection() {
        return normSection;
    }

    public void setNormSection(StringFilter normSection) {
        this.normSection = normSection;
    }

    public StringFilter getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(StringFilter videoUrl) {
        this.videoUrl = videoUrl;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getQuizId() {
        return quizId;
    }

    public void setQuizId(LongFilter quizId) {
        this.quizId = quizId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }

    @Override
    public String toString() {
        return "SectionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (notes != null ? "notes=" + notes + ", " : "") +
                (normSection != null ? "normSection=" + normSection + ", " : "") +
                (videoUrl != null ? "videoUrl=" + videoUrl + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (quizId != null ? "quizId=" + quizId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
                (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            "}";
    }

}
