package com.zony.app.service.dto;

import com.zony.app.enums.SystemDocAttachEnum;
import com.zony.common.base.BaseDTO;
import lombok.Data;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.Map;

@Data
public class FileAttachDto extends BaseDTO implements Serializable {
    private String md5;//附件md5值
    private String fileName;//附件名称
    private Long id;
    private String fnId;
    private Map<String,Object> otherInfo;  //存放添加附件时其他信息
    @Convert(converter = SystemDocAttachEnum.EnumConverter.class)
    private SystemDocAttachEnum systemDocType;
}
