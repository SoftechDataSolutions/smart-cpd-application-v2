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

    private InstantFilter loggedin;

    private InstantFilter loggedout;

    private LongFilter timespent;

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

    public InstantFilter getLoggedin() {
        return loggedin;
    }

    public void setLoggedin(InstantFilter loggedin) {
        this.loggedin = loggedin;
    }

    public InstantFilter getLoggedout() {
        return loggedout;
    }

    public void setLoggedout(InstantFilter loggedout) {
        this.loggedout = loggedout;
    }

    public LongFilter getTimespent() {
        return timespent;
    }

    public void setTimespent(LongFilter timespent) {
        this.timespent = timespent;
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
                (loggedin != null ? "loggedin=" + loggedin + ", " : "") +
                (loggedout != null ? "loggedout=" + loggedout + ", " : "") +
                (timespent != null ? "timespent=" + timespent + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
            "}";
    }

}
