package io.github.softech.dev.sgill.web.rest.vm;

import io.github.softech.dev.sgill.service.dto.UserDTO;
import javax.validation.constraints.Size;
import io.github.softech.dev.sgill.domain.enumeration.TYPES;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private String phone;

    private String streetaddress;

    @NotNull
    private String postalcode;

    @NotNull
    private String city;

    @NotNull
    private String stateProvince;

    @NotNull
    private String country;

    private byte[] profilePic;

    private String profilePicContentType;

    private Instant registered;

    private Instant lastactive;

    private Integer points;

    private ZonedDateTime cycledate;

    private String areaserviced;

    @Enumerated(EnumType.STRING)
    private TYPES specialities;

    private String trades;

    @NotNull
    private String monthYear;

    private String licenseNumber;

    private Company company;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicContentType() {
        return profilePicContentType;
    }

    public void setProfilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
    }

    public Instant getRegistered() {
        return registered;
    }

    public void setRegistered(Instant registered) {
        this.registered = registered;
    }

    public Instant getLastactive() {
        return lastactive;
    }

    public void setLastactive(Instant lastactive) {
        this.lastactive = lastactive;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ZonedDateTime getCycledate() {
        return cycledate;
    }

    public void setCycledate(ZonedDateTime cycledate) {
        this.cycledate = cycledate;
    }

    public String getAreaserviced() {
        return areaserviced;
    }

    public void setAreaserviced(String areaserviced) {
        this.areaserviced = areaserviced;
    }

    public TYPES getSpecialities() {
        return specialities;
    }

    public void setSpecialities(TYPES specialities) {
        this.specialities = specialities;
    }

    public String getTrades() {
        return trades;
    }

    public void setTrades(String trades) {
        this.trades = trades;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" +
            "} " + super.toString();
    }
}
