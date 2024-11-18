/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import com.zony.common.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/10/12 -13:49 
 */
@Data
public class SystemDocumentSmall extends BaseEntity implements Serializable {
    private Long id;

    private String systemTitle;

    private String systemCode;

}
