package com.zony.app.domain.vo;

import com.zony.app.service.dto.FileAttachDto;
import com.zony.common.base.BaseEntity;
import lombok.Data;
import java.util.List;

/**
 * 带电子附件的通用表单对象
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Data
public class AttachFormVo {
    private BaseEntity formObj;//表单元数据对象
    private List<FileAttachDto> attachList;//附件集合
}
