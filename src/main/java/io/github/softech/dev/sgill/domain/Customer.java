package io.github.softech.dev.sgill.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

import io.github.softech.dev.sgill.domain.enumeration.TYPES;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "normalized")
    private String normalized;

    @NotNull
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-.●]?([0-9]{3})[-.●]?([0-9]{4})$")
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "streetaddress", nullable = false)
    private String streetaddress;

    @NotNull
    @Pattern(regexp = "^\\d{5}-\\d{4}|\\d{5}|[A-Z]\\d[A-Z] \\d[A-Z]\\d$")
    @Column(name = "postalcode", nullable = false)
    private String postalcode;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "state_province", nullable = false)
    private String stateProvince;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Lob
    @Column(name = "profile_pic")
    private byte[] profilePic;

    @Column(name = "profile_pic_content_type")
    private String profilePicContentType;

    @Column(name = "registered")
    private Instant registered;

    @Column(name = "lastactive")
    private Instant lastactive;

    @Column(name = "points")
    private Integer points;

    @Column(name = "cycledate")
    private ZonedDateTime cycledate;

    @Column(name = "areaserviced")
    private String areaserviced;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialities")
    private TYPES specialities;

    @Column(name = "trades")
    private String trades;

    @NotNull
    @Column(name = "month_year", nullable = false)
    private String monthYear;

    @Column(name = "license_number")
    private String licenseNumber;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNormalized() {
        return normalized;
    }

    public Customer normalized(String normalized) {
        this.normalized = normalized;
        return this;
    }

    public void setNormalized(String normalized) {
        this.normalized = normalized;
    }

    public String getPhone() {
        return phone;
    }

    public Customer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public Customer streetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
        return this;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public Customer postalcode(String postalcode) {
        this.postalcode = postalcode;
        return this;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCity() {
        return city;
    }

    public Customer city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public Customer stateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountry() {
        return country;
    }

    public Customer country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public Customer profilePic(byte[] profilePic) {
        this.profilePic = profilePic;
        return this;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicContentType() {
        return profilePicContentType;
    }

    public Customer profilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
        return this;
    }

    public void setProfilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
    }

    public Instant getRegistered() {
        return registered;
    }

    public Customer registered(Instant registered) {
        this.registered = registered;
        return this;
    }

    public void setRegistered(Instant registered) {
        this.registered = registered;
    }

    public Instant getLastactive() {
        return lastactive;
    }

    public Customer lastactive(Instant lastactive) {
        this.lastactive = lastactive;
        return this;
    }

    public void setLastactive(Instant lastactive) {
        this.lastactive = lastactive;
    }

    public Integer getPoints() {
        return points;
    }

    public Customer points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ZonedDateTime getCycledate() {
        return cycledate;
    }

    public Customer cycledate(ZonedDateTime cycledate) {
        this.cycledate = cycledate;
        return this;
    }

    public void setCycledate(ZonedDateTime cycledate) {
        this.cycledate = cycledate;
    }

    public String getAreaserviced() {
        return areaserviced;
    }

    public Customer areaserviced(String areaserviced) {
        this.areaserviced = areaserviced;
        return this;
    }

    public void setAreaserviced(String areaserviced) {
        this.areaserviced = areaserviced;
    }

    public TYPES getSpecialities() {
        return specialities;
    }

    public Customer specialities(TYPES specialities) {
        this.specialities = specialities;
        return this;
    }

    public void setSpecialities(TYPES specialities) {
        this.specialities = specialities;
    }

    public String getTrades() {
        return trades;
    }

    public Customer trades(String trades) {
        this.trades = trades;
        return this;
    }

    public void setTrades(String trades) {
        this.trades = trades;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public Customer monthYear(String monthYear) {
        this.monthYear = monthYear;
        return this;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public Customer licenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
        return this;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Company getCompany() {
        return company;
    }

    public Customer company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", normalized='" + getNormalized() + "'" +
            ", phone='" + getPhone() + "'" +
            ", streetaddress='" + getStreetaddress() + "'" +
            ", postalcode='" + getPostalcode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", country='" + getCountry() + "'" +
            ", profilePic='" + getProfilePic() + "'" +
            ", profilePicContentType='" + getProfilePicContentType() + "'" +
            ", registered='" + getRegistered() + "'" +
            ", lastactive='" + getLastactive() + "'" +
            ", points=" + getPoints() +
            ", cycledate='" + getCycledate() + "'" +
            ", areaserviced='" + getAreaserviced() + "'" +
            ", specialities='" + getSpecialities() + "'" +
            ", trades='" + getTrades() + "'" +
            ", monthYear='" + getMonthYear() + "'" +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            "}";
    }
}
