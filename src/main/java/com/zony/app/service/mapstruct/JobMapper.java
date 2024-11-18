
package com.zony.app.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.app.domain.Job;
import com.zony.app.service.dto.JobDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",uses = {DeptMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapper extends BaseMapper<JobDto, Job> {
}