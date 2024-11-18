/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.repository;

import com.zony.app.domain.WorkflowCommon;
import com.zony.app.domain.WorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/14 -13:44 
 */
public interface WorkflowLogRepository extends JpaRepository<WorkflowLog, Long>, JpaSpecificationExecutor<WorkflowLog> {

    List<WorkflowLog> findByWorkflowCommonAndStepLength(WorkflowCommon workflowCommon, Integer stepLength);

    List<WorkflowLog> findByWorkflowCommonAndStepLengthLessThanEqual(WorkflowCommon workflowCommon, Integer stepLength);

    Integer countByWorkflowCommonAndStepLengthAndUsername(WorkflowCommon workflowCommon,Integer stepLength,String username);

    List<WorkflowLog> findByWorkflowCommon(WorkflowCommon workflowCommon);

    List<WorkflowLog> findByWorkflowCommonIn(List<WorkflowCommon> workflowCommonList);
}
