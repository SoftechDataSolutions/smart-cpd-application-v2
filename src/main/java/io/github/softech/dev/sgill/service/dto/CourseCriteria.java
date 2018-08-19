package io.github.softech.dev.sgill.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Course entity. This class is used in CourseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /courses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter section;

    private StringFilter normCourses;

    private StringFilter description;

    private DoubleFilter amount;

    private ZonedDateTimeFilter startdate;

    private ZonedDateTimeFilter enddate;

    private LongFilter point;

    private StringFilter credit;

    private StringFilter country;

    private StringFilter state;

    private LongFilter topicId;

    public CourseCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSection() {
        return section;
    }

    public void setSection(StringFilter section) {
        this.section = section;
    }

    public StringFilter getNormCourses() {
        return normCourses;
    }

    public void setNormCourses(StringFilter normCourses) {
        this.normCourses = normCourses;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public ZonedDateTimeFilter getStartdate() {
        return startdate;
    }

    public void setStartdate(ZonedDateTimeFilter startdate) {
        this.startdate = startdate;
    }

    public ZonedDateTimeFilter getEnddate() {
        return enddate;
    }

    public void setEnddate(ZonedDateTimeFilter enddate) {
        this.enddate = enddate;
    }

    public LongFilter getPoint() {
        return point;
    }

    public void setPoint(LongFilter point) {
        this.point = point;
    }

    public StringFilter getCredit() {
        return credit;
    }

    public void setCredit(StringFilter credit) {
        this.credit = credit;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getState() {
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public LongFilter getTopicId() {
        return topicId;
    }

    public void setTopicId(LongFilter topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return "CourseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (section != null ? "section=" + section + ", " : "") +
                (normCourses != null ? "normCourses=" + normCourses + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (startdate != null ? "startdate=" + startdate + ", " : "") +
                (enddate != null ? "enddate=" + enddate + ", " : "") +
                (point != null ? "point=" + point + ", " : "") +
                (credit != null ? "credit=" + credit + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (topicId != null ? "topicId=" + topicId + ", " : "") +
            "}";
    }

}
