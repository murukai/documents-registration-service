package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Applicant} and its DTO {@link ApplicantDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, AppointmentSlotMapper.class })
public interface ApplicantMapper extends EntityMapper<ApplicantDTO, Applicant> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "appointmentSlot", source = "appointmentSlot", qualifiedByName = "timeOfAppointment")
    ApplicantDTO toDto(Applicant s);

    @Named("lastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    ApplicantDTO toDtoLastName(Applicant applicant);
}
