package com.zony.app.service.mapstruct;

import com.zony.app.domain.Baselibrary;
import com.zony.app.domain.Jurisdiction;
import com.zony.app.service.dto.BaselibraryDto;
import com.zony.app.service.dto.JurisdictionDto;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JurisdictionMapper extends BaseMapper<JurisdictionDto, Jurisdiction>{
}
