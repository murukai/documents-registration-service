package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppointmentSettings} and its DTO {@link AppointmentSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppointmentSettingsMapper extends EntityMapper<AppointmentSettingsDTO, AppointmentSettings> {}
