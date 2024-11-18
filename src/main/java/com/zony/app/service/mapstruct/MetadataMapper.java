package com.zony.app.service.mapstruct;

import com.zony.app.domain.Metadata;
import com.zony.app.service.dto.MetadataDto;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetadataMapper extends BaseMapper<MetadataDto, Metadata>{
}
