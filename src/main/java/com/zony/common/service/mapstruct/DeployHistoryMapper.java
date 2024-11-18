
package com.zony.common.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.common.domain.DeployHistory;
import com.zony.common.service.dto.DeployHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployHistoryMapper extends BaseMapper<DeployHistoryDto, DeployHistory> {

}
