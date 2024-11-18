
package com.zony.common.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.common.domain.Deploy;
import com.zony.common.service.dto.DeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",uses = {AppMapper.class, ServerDeployMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployMapper extends BaseMapper<DeployDto, Deploy> {

}
