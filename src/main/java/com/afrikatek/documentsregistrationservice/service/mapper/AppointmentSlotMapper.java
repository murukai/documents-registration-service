package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSlotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppointmentSlot} and its DTO {@link AppointmentSlotDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppointmentMapper.class })
public interface AppointmentSlotMapper extends EntityMapper<AppointmentSlotDTO, AppointmentSlot> {
    @Mapping(target = "appointment", source = "appointment", qualifiedByName = "id")
    AppointmentSlotDTO toDto(AppointmentSlot s);

    @Named("timeOfAppointment")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "timeOfAppointment", source = "timeOfAppointment")
    AppointmentSlotDTO toDtoTimeOfAppointment(AppointmentSlot appointmentSlot);
}
