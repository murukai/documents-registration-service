package com.afrikatek.documentsregistrationservice.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "country")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "country_name", nullable = false)
    private String countryName;

    @NotNull
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @NotNull
    @Column(name = "calling_code", nullable = false)
    private String callingCode;

    @Column(name = "sub_region")
    private String subRegion;

    @Column(name = "region")
    private String region;

    @Column(name = "population")
    private Integer population;

    @Size(max = 15)
    @Column(name = "time_zone", length = 15)
    private String timeZone;

    @Column(name = "numeric_code")
    private Integer numericCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country id(Long id) {
        this.id = id;
        return this;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public Country countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public Country countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCallingCode() {
        return this.callingCode;
    }

    public Country callingCode(String callingCode) {
        this.callingCode = callingCode;
        return this;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public String getSubRegion() {
        return this.subRegion;
    }

    public Country subRegion(String subRegion) {
        this.subRegion = subRegion;
        return this;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getRegion() {
        return this.region;
    }

    public Country region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getPopulation() {
        return this.population;
    }

    public Country population(Integer population) {
        this.population = population;
        return this;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public Country timeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getNumericCode() {
        return this.numericCode;
    }

    public Country numericCode(Integer numericCode) {
        this.numericCode = numericCode;
        return this;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
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
