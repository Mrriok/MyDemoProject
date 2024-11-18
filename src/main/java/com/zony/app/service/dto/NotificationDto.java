/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.dto;

import com.zony.app.domain.User;
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
 * @date 2020/7/13 -10:00 
 */
@Data
public class NotificationDto extends BaseDTO implements Serializable {

    private Long id;

    private String messageTitle;

    private String messageContent;

    private String toSomeone;

    private List<User> toSomeoneList;

    private User initUser;

    private Boolean readFlag;

    private Boolean isDelete;
}
