package com.zony.app.service.mapstruct;

import com.zony.app.domain.Regulation;
import com.zony.app.service.dto.RegulationDto;
import com.zony.app.service.dto.SystemDocumentSmallDto;
import com.zony.common.base.BaseMapper;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegulationMapper extends BaseMapper<RegulationDto, Regulation>{
    default List<String> jsonToList(String jsonValue) {
        if (StringUtils.isNotBlank(jsonValue)) {
            return (List<String>) JsonUtil.getInstance().json2obj(jsonValue, List.class);
        } else {
            return null;
        }
    }

    default String listToJson(List<String> valueList) {
        if (valueList != null && !valueList.isEmpty()) {
            return JsonUtil.getInstance().obj2json(valueList);
        } else {
            return null;
        }
    }
    //
    //default List<Map<String,Object>> jsonToDocAccordingList(String jsonValue) {
    //    if (StringUtils.isNotBlank(jsonValue)) {
    //        return (List<Map<String, Object>>) JsonUtil.getInstance().json2obj(jsonValue, List.class);
    //    } else {
    //        return null;
    //    }
    //}
    //
    //default String docAccordingListToJson(List<Map<String,Object>> valueList) {
    //    if (valueList != null && !valueList.isEmpty()) {
    //        return JsonUtil.getInstance().obj2json(valueList);
    //    } else {
    //        return null;
    //    }
    //}

}
