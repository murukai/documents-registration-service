package com.afrikatek.documentsregistrationservice.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.AppointmentSlot} entity.
 */
public class AppointmentSlotDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant timeOfAppointment;

    @NotNull
    private Boolean available;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private Integer maxAppointments;

    private AppointmentDTO appointment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimeOfAppointment() {
        return timeOfAppointment;
    }

    public void setTimeOfAppointment(Instant timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getMaxAppointments() {
        return maxAppointments;
    }

    public void setMaxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
    }

    public AppointmentDTO getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentDTO appointment) {
        this.appointment = appointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentSlotDTO)) {
            return false;
        }

        AppointmentSlotDTO appointmentSlotDTO = (AppointmentSlotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentSlotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentSlotDTO{" +
            "id=" + getId() +
            ", timeOfAppointment='" + getTimeOfAppointment() + "'" +
            ", available='" + getAvailable() + "'" +
            ", maxAppointments=" + getMaxAppointments() +
            ", appointment=" + getAppointment() +
            "}";
    }
}
