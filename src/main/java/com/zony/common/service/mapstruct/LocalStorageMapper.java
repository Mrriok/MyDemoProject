
package com.zony.common.service.mapstruct;

import com.zony.common.base.BaseMapper;
import com.zony.common.service.dto.LocalStorageDto;
import com.zony.common.domain.LocalStorage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapper extends BaseMapper<LocalStorageDto, LocalStorage> {

}