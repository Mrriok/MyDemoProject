package com.zony.app.service.mapstruct;

import com.zony.app.domain.Baselibrary;
import com.zony.app.service.dto.BaselibraryDto;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaselibraryMapper extends  BaseMapper<BaselibraryDto, Baselibrary>{
}
