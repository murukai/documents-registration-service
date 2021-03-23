package com.afrikatek.documentsregistrationservice.service.dto;

import com.afrikatek.documentsregistrationservice.domain.enumeration.HolidayMonth;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsregistrationservice.domain.Holiday} entity.
 */
public class HolidayDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private HolidayMonth month;

    @NotNull
    @Min(value = 1)
    @Max(value = 31)
    private Integer day;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HolidayMonth getMonth() {
        return month;
    }

    public void setMonth(HolidayMonth month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HolidayDTO)) {
            return false;
        }

        HolidayDTO holidayDTO = (HolidayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, holidayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HolidayDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", month='" + getMonth() + "'" +
            ", day=" + getDay() +
            "}";
    }
}
