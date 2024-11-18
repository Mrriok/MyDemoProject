package com.zony.app.domain.vo;

import com.zony.app.domain.Help;
import lombok.Data;

/**
 * 制度文档表单对象
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Data
public class HelpDocumentFormVo extends AttachFormVo {

    private Help formObj;//表单元数据对象
}
