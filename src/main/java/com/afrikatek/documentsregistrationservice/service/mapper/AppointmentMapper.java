package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppointmentDTO toDtoId(Appointment appointment);
}
