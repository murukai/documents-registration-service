package com.afrikatek.documentsregistrationservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A MarriageDetails.
 */
@Entity
@Table(name = "marriage_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "marriagedetails")
public class MarriageDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_of_marriage", nullable = false)
    private LocalDate dateOfMarriage;

    @NotNull
    @Column(name = "spouse_full_name", nullable = false)
    private String spouseFullName;

    @NotNull
    @Column(name = "place_of_marriage", nullable = false)
    private String placeOfMarriage;

    @NotNull
    @Column(name = "spouse_place_of_birth", nullable = false)
    private String spousePlaceOfBirth;

    @NotNull
    @Column(name = "marriage_number", nullable = false)
    private String marriageNumber;

    @NotNull
    @Column(name = "married_before", nullable = false)
    private Boolean marriedBefore;

    @JsonIgnoreProperties(value = { "addresses", "user", "marriageDetails", "nextOfKeen", "appointmentSlot" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Applicant applicant;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Country countryOfMarriage;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Country spouseCountryOfBirth;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MarriageDetails id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateOfMarriage() {
        return this.dateOfMarriage;
    }

    public MarriageDetails dateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
        return this;
    }

    public void setDateOfMarriage(LocalDate dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public String getSpouseFullName() {
        return this.spouseFullName;
    }

    public MarriageDetails spouseFullName(String spouseFullName) {
        this.spouseFullName = spouseFullName;
        return this;
    }

    public void setSpouseFullName(String spouseFullName) {
        this.spouseFullName = spouseFullName;
    }

    public String getPlaceOfMarriage() {
        return this.placeOfMarriage;
    }

    public MarriageDetails placeOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
        return this;
    }

    public void setPlaceOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public String getSpousePlaceOfBirth() {
        return this.spousePlaceOfBirth;
    }

    public MarriageDetails spousePlaceOfBirth(String spousePlaceOfBirth) {
        this.spousePlaceOfBirth = spousePlaceOfBirth;
        return this;
    }

    public void setSpousePlaceOfBirth(String spousePlaceOfBirth) {
        this.spousePlaceOfBirth = spousePlaceOfBirth;
    }

    public String getMarriageNumber() {
        return this.marriageNumber;
    }

    public MarriageDetails marriageNumber(String marriageNumber) {
        this.marriageNumber = marriageNumber;
        return this;
    }

    public void setMarriageNumber(String marriageNumber) {
        this.marriageNumber = marriageNumber;
    }

    public Boolean getMarriedBefore() {
        return this.marriedBefore;
    }

    public MarriageDetails marriedBefore(Boolean marriedBefore) {
        this.marriedBefore = marriedBefore;
        return this;
    }

    public void setMarriedBefore(Boolean marriedBefore) {
        this.marriedBefore = marriedBefore;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public MarriageDetails applicant(Applicant applicant) {
        this.setApplicant(applicant);
        return this;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public Country getCountryOfMarriage() {
        return this.countryOfMarriage;
    }

    public MarriageDetails countryOfMarriage(Country country) {
        this.setCountryOfMarriage(country);
        return this;
    }

    public void setCountryOfMarriage(Country country) {
        this.countryOfMarriage = country;
    }

    public Country getSpouseCountryOfBirth() {
        return this.spouseCountryOfBirth;
    }

    public MarriageDetails spouseCountryOfBirth(Country country) {
        this.setSpouseCountryOfBirth(country);
        return this;
    }

    public void setSpouseCountryOfBirth(Country country) {
        this.spouseCountryOfBirth = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarriageDetails)) {
            return false;
        }
        return id != null && id.equals(((MarriageDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarriageDetails{" +
            "id=" + getId() +
            ", dateOfMarriage='" + getDateOfMarriage() + "'" +
            ", spouseFullName='" + getSpouseFullName() + "'" +
            ", placeOfMarriage='" + getPlaceOfMarriage() + "'" +
            ", spousePlaceOfBirth='" + getSpousePlaceOfBirth() + "'" +
            ", marriageNumber='" + getMarriageNumber() + "'" +
            ", marriedBefore='" + getMarriedBefore() + "'" +
            "}";
    }
}
