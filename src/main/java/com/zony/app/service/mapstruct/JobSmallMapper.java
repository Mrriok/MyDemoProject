
package com.zony.app.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.app.domain.Job;
import com.zony.app.service.dto.JobSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobSmallMapper extends BaseMapper<JobSmallDto, Job> {

}