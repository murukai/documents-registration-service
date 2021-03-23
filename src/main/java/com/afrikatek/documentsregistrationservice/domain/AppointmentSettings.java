package com.afrikatek.documentsregistrationservice.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A AppointmentSettings.
 */
@Entity
@Table(name = "appointment_settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appointmentsettings")
public class AppointmentSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    @Column(name = "max_ordinary_appointments", nullable = false)
    private Integer maxOrdinaryAppointments;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    @Column(name = "max_emergency_appointments", nullable = false)
    private Integer maxEmergencyAppointments;

    @NotNull
    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "starting_work_time", nullable = false)
    private Integer startingWorkTime;

    @NotNull
    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "ending_work_time", nullable = false)
    private Integer endingWorkTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppointmentSettings id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getMaxOrdinaryAppointments() {
        return this.maxOrdinaryAppointments;
    }

    public AppointmentSettings maxOrdinaryAppointments(Integer maxOrdinaryAppointments) {
        this.maxOrdinaryAppointments = maxOrdinaryAppointments;
        return this;
    }

    public void setMaxOrdinaryAppointments(Integer maxOrdinaryAppointments) {
        this.maxOrdinaryAppointments = maxOrdinaryAppointments;
    }

    public Integer getMaxEmergencyAppointments() {
        return this.maxEmergencyAppointments;
    }

    public AppointmentSettings maxEmergencyAppointments(Integer maxEmergencyAppointments) {
        this.maxEmergencyAppointments = maxEmergencyAppointments;
        return this;
    }

    public void setMaxEmergencyAppointments(Integer maxEmergencyAppointments) {
        this.maxEmergencyAppointments = maxEmergencyAppointments;
    }

    public Integer getStartingWorkTime() {
        return this.startingWorkTime;
    }

    public AppointmentSettings startingWorkTime(Integer startingWorkTime) {
        this.startingWorkTime = startingWorkTime;
        return this;
    }

    public void setStartingWorkTime(Integer startingWorkTime) {
        this.startingWorkTime = startingWorkTime;
    }

    public Integer getEndingWorkTime() {
        return this.endingWorkTime;
    }

    public AppointmentSettings endingWorkTime(Integer endingWorkTime) {
        this.endingWorkTime = endingWorkTime;
        return this;
    }

    public void setEndingWorkTime(Integer endingWorkTime) {
        this.endingWorkTime = endingWorkTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentSettings)) {
            return false;
        }
        return id != null && id.equals(((AppointmentSettings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentSettings{" +
            "id=" + getId() +
            ", maxOrdinaryAppointments=" + getMaxOrdinaryAppointments() +
            ", maxEmergencyAppointments=" + getMaxEmergencyAppointments() +
            ", startingWorkTime=" + getStartingWorkTime() +
            ", endingWorkTime=" + getEndingWorkTime() +
            "}";
    }
}
