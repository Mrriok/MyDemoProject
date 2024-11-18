/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.dto;

import lombok.Data;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/10/12 -9:51 
 */
@Data
public class SystemDocumentSmallDto {

    private String systemTitle;

    private String systemCode;

    public SystemDocumentSmallDto(){

    }
    public SystemDocumentSmallDto(String systemCode, String systemTitle) {
        this.systemCode = systemCode;
        this.systemTitle = systemTitle;
    }
}
