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
 * @date 2020/10/10 -17:10
 */
@Data
public class DocExplainDto {

    private String value;

    private String companyName;

    public DocExplainDto(){

    }

    public DocExplainDto(String value, String companyName) {
        this.value = value;
        this.companyName = companyName;
    }
}
