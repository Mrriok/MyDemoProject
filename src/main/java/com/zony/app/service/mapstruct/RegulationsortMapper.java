package com.zony.app.service.mapstruct;

import com.zony.app.domain.Regulationsort;
import com.zony.app.service.dto.RegulationsortDto;
import com.zony.app.service.dto.SystemDocumentSmallDto;
import com.zony.common.base.BaseMapper;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegulationsortMapper extends BaseMapper<RegulationsortDto, Regulationsort> {

    default List<SystemDocumentSmallDto> jsonToDocAccordingList(String jsonValue) {
        if (StringUtils.isNotBlank(jsonValue)) {
            return JsonUtil.getInstance().json2list(jsonValue, SystemDocumentSmallDto.class);
        } else {
            return null;
        }
    }

    default String docAccordingListToJson(List<SystemDocumentSmallDto> valueList) {
        if (valueList != null && !valueList.isEmpty()) {
            return JsonUtil.getInstance().obj2json(valueList);
        } else {
            return null;
        }
    }
}
