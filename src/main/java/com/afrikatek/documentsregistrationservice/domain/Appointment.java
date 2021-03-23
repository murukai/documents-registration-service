package com.afrikatek.documentsregistrationservice.domain;

import com.afrikatek.documentsregistrationservice.domain.enumeration.AppointmentType;
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
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_of_appointment", nullable = false)
    private Instant dateOfAppointment;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type")
    private AppointmentType appointmentType;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    @Column(name = "max_appointments", nullable = false)
    private Integer maxAppointments;

    @OneToMany(mappedBy = "appointment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicants", "appointment" }, allowSetters = true)
    private Set<AppointmentSlot> appointmentSlots = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDateOfAppointment() {
        return this.dateOfAppointment;
    }

    public Appointment dateOfAppointment(Instant dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
        return this;
    }

    public void setDateOfAppointment(Instant dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public Boolean getAvailable() {
        return this.available;
    }

    public Appointment available(Boolean available) {
        this.available = available;
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public AppointmentType getAppointmentType() {
        return this.appointmentType;
    }

    public Appointment appointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
        return this;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Integer getMaxAppointments() {
        return this.maxAppointments;
    }

    public Appointment maxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
        return this;
    }

    public void setMaxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
    }

    public Set<AppointmentSlot> getAppointmentSlots() {
        return this.appointmentSlots;
    }

    public Appointment appointmentSlots(Set<AppointmentSlot> appointmentSlots) {
        this.setAppointmentSlots(appointmentSlots);
        return this;
    }

    public Appointment addAppointmentSlot(AppointmentSlot appointmentSlot) {
        this.appointmentSlots.add(appointmentSlot);
        appointmentSlot.setAppointment(this);
        return this;
    }

    public Appointment removeAppointmentSlot(AppointmentSlot appointmentSlot) {
        this.appointmentSlots.remove(appointmentSlot);
        appointmentSlot.setAppointment(null);
        return this;
    }

    public void setAppointmentSlots(Set<AppointmentSlot> appointmentSlots) {
        if (this.appointmentSlots != null) {
            this.appointmentSlots.forEach(i -> i.setAppointment(null));
        }
        if (appointmentSlots != null) {
            appointmentSlots.forEach(i -> i.setAppointment(this));
        }
        this.appointmentSlots = appointmentSlots;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointment)) {
            return false;
        }
        return id != null && id.equals(((Appointment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + getId() +
            ", dateOfAppointment='" + getDateOfAppointment() + "'" +
            ", available='" + getAvailable() + "'" +
            ", appointmentType='" + getAppointmentType() + "'" +
            ", maxAppointments=" + getMaxAppointments() +
            "}";
    }
}
