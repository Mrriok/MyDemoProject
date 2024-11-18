/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.dto;

import com.zony.app.domain.Dept;
import com.zony.app.domain.User;
import com.zony.common.base.BaseDTO;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/1 -16:39 
 */
public class FeedbackOpinionDto extends BaseDTO implements Serializable {

    private Long id;

    private DeptDto initDept;

    private UserDto initUser;

    private String chapter;

    private String opinionContent;

    private Boolean agreeFlag;

    private String rejectReason;

    private Long workflowCommonId;

    private Integer stepSeqNum;

    private String feedbackTypeIndex;
}
