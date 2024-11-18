package com.zony.app.service.dto;

import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.vo.AttachFormVo;
import lombok.Data;

/**
 * 制度文档表单对象
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Data
public class SystemDocumentAttachDto extends AttachFormVo {
    private SystemDocument formObj;//表单元数据对象
}
