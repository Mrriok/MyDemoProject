package com.zony.app.service.mapstruct;

import com.zony.app.domain.Baselibrary;
import com.zony.app.domain.Collect;
import com.zony.app.service.dto.BaselibraryDto;
import com.zony.app.service.dto.CollectDto;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CollectMapper extends BaseMapper<CollectDto, Collect>{
}
