package com.afrikatek.documentsregistrationservice.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.MarriageDetails} entity.
 */
public class MarriageDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateOfMarriage;

    @NotNull
    private String spouseFullName;

    @NotNull
    private String placeOfMarriage;

    @NotNull
    private String spousePlaceOfBirth;

    @NotNull
    private String marriageNumber;

    @NotNull
    private Boolean marriedBefore;

    private ApplicantDTO applicant;

    private CountryDTO countryOfMarriage;

    private CountryDTO spouseCountryOfBirth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public String getSpouseFullName() {
        return spouseFullName;
    }

    public void setSpouseFullName(String spouseFullName) {
        this.spouseFullName = spouseFullName;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public String getSpousePlaceOfBirth() {
        return spousePlaceOfBirth;
    }

    public void setSpousePlaceOfBirth(String spousePlaceOfBirth) {
        this.spousePlaceOfBirth = spousePlaceOfBirth;
    }

    public String getMarriageNumber() {
        return marriageNumber;
    }

    public void setMarriageNumber(String marriageNumber) {
        this.marriageNumber = marriageNumber;
    }

    public Boolean getMarriedBefore() {
        return marriedBefore;
    }

    public void setMarriedBefore(Boolean marriedBefore) {
        this.marriedBefore = marriedBefore;
    }

    public ApplicantDTO getApplicant() {
        return applicant;
    }

    public void setApplicant(ApplicantDTO applicant) {
        this.applicant = applicant;
    }

    public CountryDTO getCountryOfMarriage() {
        return countryOfMarriage;
    }

    public void setCountryOfMarriage(CountryDTO countryOfMarriage) {
        this.countryOfMarriage = countryOfMarriage;
    }

    public CountryDTO getSpouseCountryOfBirth() {
        return spouseCountryOfBirth;
    }

    public void setSpouseCountryOfBirth(CountryDTO spouseCountryOfBirth) {
        this.spouseCountryOfBirth = spouseCountryOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarriageDetailsDTO)) {
            return false;
        }

        MarriageDetailsDTO marriageDetailsDTO = (MarriageDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marriageDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarriageDetailsDTO{" +
            "id=" + getId() +
            ", dateOfMarriage='" + getDateOfMarriage() + "'" +
            ", spouseFullName='" + getSpouseFullName() + "'" +
            ", placeOfMarriage='" + getPlaceOfMarriage() + "'" +
            ", spousePlaceOfBirth='" + getSpousePlaceOfBirth() + "'" +
            ", marriageNumber='" + getMarriageNumber() + "'" +
            ", marriedBefore='" + getMarriedBefore() + "'" +
            ", applicant=" + getApplicant() +
            ", countryOfMarriage=" + getCountryOfMarriage() +
            ", spouseCountryOfBirth=" + getSpouseCountryOfBirth() +
            "}";
    }
}
