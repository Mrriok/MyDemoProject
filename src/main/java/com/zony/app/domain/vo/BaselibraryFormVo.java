package com.zony.app.domain.vo;

import com.zony.app.domain.Baselibrary;
import com.zony.app.domain.SystemDocument;
import com.zony.app.service.dto.BaselibraryDto;
import com.zony.app.service.dto.SystemDocumentDto;
import lombok.Data;

/**
 * 制度文档表单对象
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Data
public class BaselibraryFormVo extends AttachFormVo {

    private Baselibrary formObj;//表单实体类对象
    private BaselibraryDto formDtoObj;//表单元数据对象
}
