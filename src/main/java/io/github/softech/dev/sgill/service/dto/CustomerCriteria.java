package io.github.softech.dev.sgill.service.dto;

import java.io.Serializable;
import io.github.softech.dev.sgill.domain.enumeration.TYPES;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;

import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Customer entity. This class is used in CustomerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /customers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable {
    /**
     * Class for filtering TYPES
     */
    public static class TYPESFilter extends Filter<TYPES> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter normalized;

    private StringFilter phone;

    private StringFilter streetaddress;

    private StringFilter postalcode;

    private StringFilter city;

    private StringFilter stateProvince;

    private StringFilter country;

    private InstantFilter registered;

    private InstantFilter lastactive;

    private IntegerFilter points;

    private ZonedDateTimeFilter cycledate;

    private StringFilter areaserviced;

    private TYPESFilter specialities;

    private StringFilter trades;

    private StringFilter monthYear;

    private StringFilter licenseNumber;

    private LongFilter companyId;

    public CustomerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNormalized() {
        return normalized;
    }

    public void setNormalized(StringFilter normalized) {
        this.normalized = normalized;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(StringFilter streetaddress) {
        this.streetaddress = streetaddress;
    }

    public StringFilter getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(StringFilter postalcode) {
        this.postalcode = postalcode;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(StringFilter stateProvince) {
        this.stateProvince = stateProvince;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public InstantFilter getRegistered() {
        return registered;
    }

    public void setRegistered(InstantFilter registered) {
        this.registered = registered;
    }

    public InstantFilter getLastactive() {
        return lastactive;
    }

    public void setLastactive(InstantFilter lastactive) {
        this.lastactive = lastactive;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public ZonedDateTimeFilter getCycledate() {
        return cycledate;
    }

    public void setCycledate(ZonedDateTimeFilter cycledate) {
        this.cycledate = cycledate;
    }

    public StringFilter getAreaserviced() {
        return areaserviced;
    }

    public void setAreaserviced(StringFilter areaserviced) {
        this.areaserviced = areaserviced;
    }

    public TYPESFilter getSpecialities() {
        return specialities;
    }

    public void setSpecialities(TYPESFilter specialities) {
        this.specialities = specialities;
    }

    public StringFilter getTrades() {
        return trades;
    }

    public void setTrades(StringFilter trades) {
        this.trades = trades;
    }

    public StringFilter getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(StringFilter monthYear) {
        this.monthYear = monthYear;
    }

    public StringFilter getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(StringFilter licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (normalized != null ? "normalized=" + normalized + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (streetaddress != null ? "streetaddress=" + streetaddress + ", " : "") +
                (postalcode != null ? "postalcode=" + postalcode + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (stateProvince != null ? "stateProvince=" + stateProvince + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (registered != null ? "registered=" + registered + ", " : "") +
                (lastactive != null ? "lastactive=" + lastactive + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (cycledate != null ? "cycledate=" + cycledate + ", " : "") +
                (areaserviced != null ? "areaserviced=" + areaserviced + ", " : "") +
                (specialities != null ? "specialities=" + specialities + ", " : "") +
                (trades != null ? "trades=" + trades + ", " : "") +
                (monthYear != null ? "monthYear=" + monthYear + ", " : "") +
                (licenseNumber != null ? "licenseNumber=" + licenseNumber + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }

}
