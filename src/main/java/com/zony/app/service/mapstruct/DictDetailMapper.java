
package com.zony.app.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.app.domain.DictDetail;
import com.zony.app.service.dto.DictDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = {DictSmallMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictDetailMapper extends BaseMapper<DictDetailDto, DictDetail> {

}