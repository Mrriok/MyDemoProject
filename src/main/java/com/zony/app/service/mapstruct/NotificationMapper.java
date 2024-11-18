/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.mapstruct;

import com.zony.app.domain.Notification;
import com.zony.app.service.dto.NotificationDto;
import com.zony.common.base.BaseMapper;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/13 -10:07 
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper extends BaseMapper<NotificationDto, Notification> {
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
}
