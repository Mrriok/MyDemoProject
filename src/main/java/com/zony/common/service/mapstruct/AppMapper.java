
package com.zony.common.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.common.domain.App;
import com.zony.common.service.dto.AppDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppMapper extends BaseMapper<AppDto, App> {

}
