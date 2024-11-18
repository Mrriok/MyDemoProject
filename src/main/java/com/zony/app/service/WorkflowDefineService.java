/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.zony.app.domain.WorkflowDefine;
import com.zony.app.enums.WorkflowClassEnum;
import com.zony.app.repository.WorkflowDefineRepository;
import com.zony.common.utils.RedisUtils;
import com.zony.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/19 -16:11 
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "workflowDefine")
public class WorkflowDefineService {
    private final WorkflowDefineRepository workflowDefineRepository;
    private final RedisUtils redisUtils;
    public WorkflowDefine queryNextStep(Map<String,Object> paramMap) throws Exception {
        if(MapUtils.isEmpty(paramMap)){
            return null;
        }
        //WorkflowClassEnum workflowType = (WorkflowClassEnum) Optional.of(MapUtils.getObject(paramMap,"workflowType"))
        //        .orElseThrow(() -> new Exception("workflowType不能为空！"));
        String workflowType = MapUtils.getString(paramMap,"workflowType");
        Integer stepSeqNum = MapUtils.getInteger(paramMap,"stepSeqNum");
        if(StringUtils.isEmpty(workflowType)){
            throw new Exception("workflowType不能为空！");
        }

        WorkflowDefine workflowDefine = workflowDefineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.getItem(workflowType),stepSeqNum);
        String redisKey = workflowType+":"+stepSeqNum;
        if(redisUtils.hasKey(redisKey)){
            return (WorkflowDefine) redisUtils.get(redisKey);
        }else {
            redisUtils.set(redisKey,workflowDefine);
        }
        return workflowDefine;
    }

}
