package com.afrikatek.documentsregistrationservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.Country} entity.
 */
public class CountryDTO implements Serializable {

    private Long id;

    @NotNull
    private String countryName;

    @NotNull
    private String countryCode;

    @NotNull
    private String callingCode;

    private String subRegion;

    private String region;

    private Integer population;

    @Size(max = 15)
    private String timeZone;

    private Integer numericCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDTO)) {
            return false;
        }

        CountryDTO countryDTO = (CountryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDTO{" +
            "id=" + getId() +
            ", countryName='" + getCountryName() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", callingCode='" + getCallingCode() + "'" +
            ", subRegion='" + getSubRegion() + "'" +
            ", region='" + getRegion() + "'" +
            ", population=" + getPopulation() +
            ", timeZone='" + getTimeZone() + "'" +
            ", numericCode=" + getNumericCode() +
            "}";
    }
}
