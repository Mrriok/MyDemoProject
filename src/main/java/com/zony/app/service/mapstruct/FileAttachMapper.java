
package com.zony.app.service.mapstruct;

import com.zony.app.domain.FileAttach;
import com.zony.app.service.dto.FileAttachDto;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileAttachMapper extends BaseMapper<FileAttachDto, FileAttach> {
}
