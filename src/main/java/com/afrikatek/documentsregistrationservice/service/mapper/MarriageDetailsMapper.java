package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.MarriageDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MarriageDetails} and its DTO {@link MarriageDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicantMapper.class, CountryMapper.class })
public interface MarriageDetailsMapper extends EntityMapper<MarriageDetailsDTO, MarriageDetails> {
    @Mapping(target = "applicant", source = "applicant", qualifiedByName = "lastName")
    @Mapping(target = "countryOfMarriage", source = "countryOfMarriage", qualifiedByName = "countryName")
    @Mapping(target = "spouseCountryOfBirth", source = "spouseCountryOfBirth", qualifiedByName = "countryName")
    MarriageDetailsDTO toDto(MarriageDetails s);
}
