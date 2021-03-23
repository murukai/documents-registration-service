package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.NextOfKeenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NextOfKeen} and its DTO {@link NextOfKeenDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicantMapper.class })
public interface NextOfKeenMapper extends EntityMapper<NextOfKeenDTO, NextOfKeen> {
    @Mapping(target = "applicant", source = "applicant", qualifiedByName = "lastName")
    NextOfKeenDTO toDto(NextOfKeen s);
}
