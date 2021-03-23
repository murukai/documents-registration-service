package com.afrikatek.documentsregistrationservice.service.dto;

import com.afrikatek.documentsregistrationservice.domain.enumeration.AppointmentType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.Appointment} entity.
 */
public class AppointmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateOfAppointment;

    @NotNull
    private Boolean available;

    private AppointmentType appointmentType;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    private Integer maxAppointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(Instant dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Integer getMaxAppointments() {
        return maxAppointments;
    }

    public void setMaxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentDTO)) {
            return false;
        }

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + getId() +
            ", dateOfAppointment='" + getDateOfAppointment() + "'" +
            ", available='" + getAvailable() + "'" +
            ", appointmentType='" + getAppointmentType() + "'" +
            ", maxAppointments=" + getMaxAppointments() +
            "}";
    }
}
