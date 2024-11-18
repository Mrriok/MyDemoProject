/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.repository;

import com.zony.app.domain.Regulation;
import com.zony.app.domain.WorkflowCommon;
import com.zony.app.domain.WorkflowRegulationPlan;
import com.zony.app.domain.WorkflowSystemDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/7 -11:37 
 */
public interface WorkflowRegulationPlanRepository extends JpaRepository<WorkflowRegulationPlan, Long>, JpaSpecificationExecutor<WorkflowRegulationPlan> {
    WorkflowRegulationPlan findByWorkflowCommon(WorkflowCommon workflowCommon);
}
