package com.afrikatek.documentsregistrationservice.service.mapper;

import com.afrikatek.documentsregistrationservice.domain.*;
import com.afrikatek.documentsregistrationservice.service.dto.HolidayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Holiday} and its DTO {@link HolidayDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HolidayMapper extends EntityMapper<HolidayDTO, Holiday> {}
