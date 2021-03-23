package com.afrikatek.documentsregistrationservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.AppointmentSettings} entity.
 */
public class AppointmentSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    private Integer maxOrdinaryAppointments;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    private Integer maxEmergencyAppointments;

    @NotNull
    @Min(value = 0)
    @Max(value = 23)
    private Integer startingWorkTime;

    @NotNull
    @Min(value = 0)
    @Max(value = 23)
    private Integer endingWorkTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxOrdinaryAppointments() {
        return maxOrdinaryAppointments;
    }

    public void setMaxOrdinaryAppointments(Integer maxOrdinaryAppointments) {
        this.maxOrdinaryAppointments = maxOrdinaryAppointments;
    }

    public Integer getMaxEmergencyAppointments() {
        return maxEmergencyAppointments;
    }

    public void setMaxEmergencyAppointments(Integer maxEmergencyAppointments) {
        this.maxEmergencyAppointments = maxEmergencyAppointments;
    }

    public Integer getStartingWorkTime() {
        return startingWorkTime;
    }

    public void setStartingWorkTime(Integer startingWorkTime) {
        this.startingWorkTime = startingWorkTime;
    }

    public Integer getEndingWorkTime() {
        return endingWorkTime;
    }

    public void setEndingWorkTime(Integer endingWorkTime) {
        this.endingWorkTime = endingWorkTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentSettingsDTO)) {
            return false;
        }

        AppointmentSettingsDTO appointmentSettingsDTO = (AppointmentSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentSettingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentSettingsDTO{" +
            "id=" + getId() +
            ", maxOrdinaryAppointments=" + getMaxOrdinaryAppointments() +
            ", maxEmergencyAppointments=" + getMaxEmergencyAppointments() +
            ", startingWorkTime=" + getStartingWorkTime() +
            ", endingWorkTime=" + getEndingWorkTime() +
            "}";
    }
}
