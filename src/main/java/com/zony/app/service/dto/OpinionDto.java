/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.Dept;
import com.zony.app.domain.User;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstPropertyEnum;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.common.base.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -15:02 
 */
@Data
public class OpinionDto extends BaseDTO implements Serializable {

    private Long id;

    private String opinionName;

    private String draftDept;

    private String instCode;

    private User initUser;

    private String instName;

    private InstPropertyEnum instProperty;

    private InstLevelEnum instLevel;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp deadline;

    private List<User> contactPersonList;

    private ProgressStatusEnum status;
}
