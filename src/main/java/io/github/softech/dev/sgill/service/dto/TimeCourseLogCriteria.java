package io.github.softech.dev.sgill.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the TimeCourseLog entity. This class is used in TimeCourseLogResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /time-course-logs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TimeCourseLogCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter timespent;

    private InstantFilter recorddate;

    private LongFilter customerId;

    private LongFilter courseId;

    public TimeCourseLogCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTimespent() {
        return timespent;
    }

    public void setTimespent(LongFilter timespent) {
        this.timespent = timespent;
    }

    public InstantFilter getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(InstantFilter recorddate) {
        this.recorddate = recorddate;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "TimeCourseLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (timespent != null ? "timespent=" + timespent + ", " : "") +
                (recorddate != null ? "recorddate=" + recorddate + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
            "}";
    }

}
