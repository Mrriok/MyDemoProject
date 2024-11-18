/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zony.app.domain.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/2 -14:00 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WorkbenchTestVo implements Serializable {

    //WorkflowEstablishment workflowEstablishment;
    //
    //WorkflowOpinion workflowOpinion;
    //
    WorkflowLogTest workflowLogTest;

    List<WorkflowFeedback> workflowFeedbackList;

    String businessType;

    Establishment establishment;

    Opinion opinion;
}
