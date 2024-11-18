
package com.zony.app.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.app.domain.Dept;
import com.zony.app.service.dto.DeptSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptSmallMapper extends BaseMapper<DeptSmallDto, Dept> {

}