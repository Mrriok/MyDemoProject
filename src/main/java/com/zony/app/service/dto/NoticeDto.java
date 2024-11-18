/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.common.base.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/8 -15:59
 */
@Data
public class NoticeDto  extends BaseDTO implements Serializable {

    private Long id;

    private String title;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp effectiveTime;

    private String content;

    private String mark;

    private String documentId;
}
