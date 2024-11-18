
package com.zony.app.service.mapstruct;

import com.zony.app.domain.Propagation;
import com.zony.app.service.dto.PropagationDetailDto;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropagationMapper extends BaseMapper<PropagationDetailDto, Propagation> {
}
