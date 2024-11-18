/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.mapstruct;

import com.zony.app.domain.WorkflowDefine;
import com.zony.app.domain.WorkflowDefineEveryOne;
import com.zony.app.domain.WorkflowNode;
import com.zony.app.domain.WorkflowNodeEveryOne;
import com.zony.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/29 -16:34 
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NodeToNodeEveryMapper extends BaseMapper<WorkflowNodeEveryOne, WorkflowNode> {
}
