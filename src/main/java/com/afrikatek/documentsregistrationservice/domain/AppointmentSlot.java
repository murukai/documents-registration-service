package com.afrikatek.documentsregistrationservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A AppointmentSlot.
 */
@Entity
@Table(name = "appointment_slot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appointmentslot")
public class AppointmentSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "time_of_appointment", nullable = false)
    private Instant timeOfAppointment;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "max_appointments", nullable = false)
    private Integer maxAppointments;

    @OneToMany(mappedBy = "appointmentSlot")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "addresses", "user", "marriageDetails", "nextOfKeen", "appointmentSlot" }, allowSetters = true)
    private Set<Applicant> applicants = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "appointmentSlots" }, allowSetters = true)
    private Appointment appointment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppointmentSlot id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getTimeOfAppointment() {
        return this.timeOfAppointment;
    }

    public AppointmentSlot timeOfAppointment(Instant timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
        return this;
    }

    public void setTimeOfAppointment(Instant timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
    }

    public Boolean getAvailable() {
        return this.available;
    }

    public AppointmentSlot available(Boolean available) {
        this.available = available;
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getMaxAppointments() {
        return this.maxAppointments;
    }

    public AppointmentSlot maxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
        return this;
    }

    public void setMaxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
    }

    public Set<Applicant> getApplicants() {
        return this.applicants;
    }

    public AppointmentSlot applicants(Set<Applicant> applicants) {
        this.setApplicants(applicants);
        return this;
    }

    public AppointmentSlot addApplicants(Applicant applicant) {
        this.applicants.add(applicant);
        applicant.setAppointmentSlot(this);
        return this;
    }

    public AppointmentSlot removeApplicants(Applicant applicant) {
        this.applicants.remove(applicant);
        applicant.setAppointmentSlot(null);
        return this;
    }

    public void setApplicants(Set<Applicant> applicants) {
        if (this.applicants != null) {
            this.applicants.forEach(i -> i.setAppointmentSlot(null));
        }
        if (applicants != null) {
            applicants.forEach(i -> i.setAppointmentSlot(this));
        }
        this.applicants = applicants;
    }

    public Appointment getAppointment() {
        return this.appointment;
    }

    public AppointmentSlot appointment(Appointment appointment) {
        this.setAppointment(appointment);
        return this;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentSlot)) {
            return false;
        }
        return id != null && id.equals(((AppointmentSlot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentSlot{" +
            "id=" + getId() +
            ", timeOfAppointment='" + getTimeOfAppointment() + "'" +
            ", available='" + getAvailable() + "'" +
            ", maxAppointments=" + getMaxAppointments() +
            "}";
    }
}
