/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.WorkflowCommon;
import com.zony.app.enums.WorkflowClassEnum;
import com.zony.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/20 -12:55
 */
@Data
public class WorkbenchLogQueryCriteria {
    @Query
    private Long id;   //log

    @Query(type = Query.Type.INNER_LIKE)
    private String workflowTitle;//制度名称  common

    @Query(type = Query.Type.INNER_LIKE)
    private Long instCode;//制度编码 common

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = WorkflowClassEnum.EnumConverter.class)
    private WorkflowClassEnum workflowType;//流程类型 common

    @Query(type = Query.Type.INNER_LIKE)
    private String workflowNum;//流程编号  common

    @Query(type = Query.Type.EQUAL)  //当前步骤  log
    private Integer stepSeqNum;

    @Query(type = Query.Type.INNER_LIKE)   //当前节点名称 log
    private String stepName;

    @Query(type = Query.Type.EQUAL)    //处理人 log
    private String username;

    @Query(type = Query.Type.EQUAL)  //log
    private String opinion;

    @Query(type = Query.Type.EQUAL)  //log
    private Boolean agreeFlag;

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> initTime;//截止时间  common

    @Query(type = Query.Type.EQUAL)
    private Long startUsername;// 发起人账户，这里为id  common

    @Query(type = Query.Type.IN)
    private List<Long> startUsernameList;  //common

    @Query(type = Query.Type.EQUAL)
    private Integer feedbackStepNum;//流程编号

    @Query(type = Query.Type.EQUAL)
    private Integer page;//流程编号

    @Query(type = Query.Type.EQUAL)
    private Integer size;//流程编号

}
