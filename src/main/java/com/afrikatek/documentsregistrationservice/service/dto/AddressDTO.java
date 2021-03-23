package com.afrikatek.documentsregistrationservice.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.Address} entity.
 */
@ApiModel(description = "not an ignored comment")
public class AddressDTO implements Serializable {

    private Long id;

    @NotNull
    private String streetAddress;

    @NotNull
    private String postalCode;

    @NotNull
    private String city;

    private String stateProvince;

    @NotNull
    private String placeOfBirth;

    private CountryDTO country;

    private CountryDTO countryOfBirth;

    private CountryDTO countryOfResidence;

    private ApplicantDTO applicant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public CountryDTO getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(CountryDTO countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public CountryDTO getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(CountryDTO countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public ApplicantDTO getApplicant() {
        return applicant;
    }

    public void setApplicant(ApplicantDTO applicant) {
        this.applicant = applicant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressDTO)) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, addressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressDTO{" +
            "id=" + getId() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", country=" + getCountry() +
            ", countryOfBirth=" + getCountryOfBirth() +
            ", countryOfResidence=" + getCountryOfResidence() +
            ", applicant=" + getApplicant() +
            "}";
    }
}
