
package com.zony.common.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.common.domain.ServerDeploy;
import com.zony.common.service.dto.ServerDeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerDeployMapper extends BaseMapper<ServerDeployDto, ServerDeploy> {

}
