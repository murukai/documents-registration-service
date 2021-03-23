package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class, ApplicantMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    @Mapping(target = "countryOfBirth", source = "countryOfBirth", qualifiedByName = "countryName")
    @Mapping(target = "countryOfResidence", source = "countryOfResidence", qualifiedByName = "countryName")
    @Mapping(target = "applicant", source = "applicant", qualifiedByName = "lastName")
    AddressDTO toDto(Address s);
}
