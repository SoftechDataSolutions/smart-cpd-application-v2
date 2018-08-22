package io.github.softech.dev.sgill.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Servicelist entity.
 */
public class ServicelistDTO implements Serializable {

    private Long id;

    private String name;

    private String company;

    private String url;

    private String phone;

    private String email;

    private String areas;

    private String speciality;

    private String trades;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getTrades() {
        return trades;
    }

    public void setTrades(String trades) {
        this.trades = trades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServicelistDTO servicelistDTO = (ServicelistDTO) o;
        if (servicelistDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), servicelistDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServicelistDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", company='" + getCompany() + "'" +
            ", url='" + getUrl() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", areas='" + getAreas() + "'" +
            ", speciality='" + getSpeciality() + "'" +
            ", trades='" + getTrades() + "'" +
            "}";
    }
}
