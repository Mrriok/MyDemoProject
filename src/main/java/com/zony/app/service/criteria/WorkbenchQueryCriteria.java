/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.WorkbenchEnum.ProcessTypeEnum;
import com.zony.app.enums.WorkflowClassEnum;
import com.zony.app.enums.WorkflowDisposeObjEnum;
import com.zony.app.enums.WorkflowDisposeWayEnum;
import com.zony.common.annotation.Query;
import lombok.Data;

import javax.persistence.Convert;
import java.sql.Timestamp;
import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/11 -16:24
 */
@Data
public class WorkbenchQueryCriteria {

    @Query
    private Long id;

    @Query(type = Query.Type.INNER_LIKE)
    private String workflowTitle;//制度名称

    @Query(type = Query.Type.INNER_LIKE)
    private Long instCode;//制度编码

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = WorkflowClassEnum.EnumConverter.class)
    private WorkflowClassEnum workflowType;//流程类型

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = WorkflowDisposeObjEnum.EnumConverter.class)
    private WorkflowDisposeObjEnum disposeObjType;//流程处理对象类型

    @Query(type = Query.Type.EQUAL)
    @Convert(converter = WorkflowDisposeWayEnum.EnumConverter.class)
    private WorkflowDisposeWayEnum disposeWayType;//流程处理方式类型

    @Query(type = Query.Type.EQUAL)
    private Integer stepSeqNum;

    @Query(type = Query.Type.EQUAL)
    private Long nowStepNum;//当前节点

    @Query(type = Query.Type.INNER_LIKE)
    private String stepName;//当前节点名称

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private List<Timestamp> initTime;//发起时间

    @Query(type = Query.Type.EQUAL)
    private Long startUsername;// 发起人账户，这里为id

    @Query(type = Query.Type.IN)
    private List<Long> startUsernameList;

    @Query(type = Query.Type.INNER_LIKE)
    private String disposeObjId;

    @Query(type = Query.Type.INNER_LIKE)
    private String workflowNum;//流程编号

    @Query(type = Query.Type.EQUAL)
    private Integer stepLength;//流程编号

    @Query(type = Query.Type.EQUAL)
    private Integer feedbackStepNum;//流程编号

    @Query(type = Query.Type.EQUAL)
    private Integer page;//流程编号

    @Query(type = Query.Type.EQUAL)
    private Integer size;//流程编号

}
