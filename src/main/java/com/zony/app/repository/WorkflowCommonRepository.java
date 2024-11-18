/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.repository;

import com.zony.app.domain.WorkflowCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/14 -13:44
 */
public interface WorkflowCommonRepository extends JpaRepository<WorkflowCommon, Long>, JpaSpecificationExecutor<WorkflowCommon> {




}
