package com.zony.app.service.mapstruct;

import com.zony.app.domain.SystemDocument;
import com.zony.app.service.dto.DocExplainDto;
import com.zony.app.service.dto.DocReplyRiskTargetDto;
import com.zony.app.service.dto.SystemDocumentDto;
import com.zony.app.service.dto.SystemDocumentSmallDto;
import com.zony.common.base.BaseMapper;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemDocumentMapper extends BaseMapper<SystemDocumentDto, SystemDocument> {

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

    default List<DocReplyRiskTargetDto> jsonToDocReplyRiskTargetList(String jsonValue) {
        if (StringUtils.isNotBlank(jsonValue)) {
            return JsonUtil.getInstance().json2list(jsonValue, DocReplyRiskTargetDto.class);
        } else {
            return null;
        }
    }

    default String docReplyRiskTargetListToJson(List<DocReplyRiskTargetDto> valueList) {
        if (valueList != null && !valueList.isEmpty()) {
            return JsonUtil.getInstance().obj2json(valueList);
        } else {
            return null;
        }
    }

    default List<DocExplainDto> jsonToDocExplainDtoList(String jsonValue) {
        if (StringUtils.isNotBlank(jsonValue)) {
            return JsonUtil.getInstance().json2list(jsonValue, DocExplainDto.class);
        } else {
            return null;
        }
    }

    default String docExplainDtoListToJson(List<DocExplainDto> valueList) {
        if (valueList != null && !valueList.isEmpty()) {
            return JsonUtil.getInstance().obj2json(valueList);
        } else {
            return null;
        }
    }

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
