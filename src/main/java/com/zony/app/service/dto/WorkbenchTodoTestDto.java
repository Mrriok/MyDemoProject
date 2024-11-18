/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.WorkflowClassEnum;
import com.zony.app.enums.WorkflowDisposeObjEnum;
import com.zony.app.enums.WorkflowDisposeWayEnum;
import com.zony.app.utils.DateUtils;
import com.zony.common.base.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/11 -17:40 
 */
@Data
@Entity
public class WorkbenchTodoTestDto extends BaseDTO implements Serializable {
    @Id
    private Long id;

    @Column(name = "workflow_type")
    @Convert(converter = WorkflowClassEnum.EnumConverter.class)
    private WorkflowClassEnum workflowType;

    private String workflowNum;

    private String workflowTitle;

    private Integer nowStepNum;

    private Long startUsername;

    private Boolean finishFlag;

    @JsonFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS, timezone=DateUtils.DEFAULT_ZONE)
    private Date createDate;

    private String stepName;

    private Integer stepSeqNum;

    private String disposeObjId;

    @Column(name = "dispose_obj_type")
    @Convert(converter = WorkflowDisposeObjEnum.EnumConverter.class)
    private WorkflowDisposeObjEnum disposeObjType;

    @Column(name = "dispose_way_type")
    @Convert(converter = WorkflowDisposeWayEnum.EnumConverter.class)
    private WorkflowDisposeWayEnum disposeWayType;

    private String organizerName;//发起人姓名

    private String workflowTypeName;

    public WorkbenchTodoTestDto() {
    }

    public WorkbenchTodoTestDto(Long id, WorkflowClassEnum workflowType, String workflowNum, String workflowTitle, Integer nowStepNum, Long startUsername, Boolean finishFlag, Date createDate, String stepName, Integer stepSeqNum, String disposeObjId, WorkflowDisposeObjEnum disposeObjType, WorkflowDisposeWayEnum disposeWayType, String organizerName) {
        this.id = id;
        this.workflowType = workflowType;
        this.workflowNum = workflowNum;
        this.workflowTitle = workflowTitle;
        this.nowStepNum = nowStepNum;
        this.startUsername = startUsername;
        this.finishFlag = finishFlag;
        this.createDate = createDate;
        this.stepName = stepName;
        this.stepSeqNum = stepSeqNum;
        this.disposeObjId = disposeObjId;
        this.disposeObjType = disposeObjType;
        this.disposeWayType = disposeWayType;
        this.organizerName = organizerName;
    }
}
