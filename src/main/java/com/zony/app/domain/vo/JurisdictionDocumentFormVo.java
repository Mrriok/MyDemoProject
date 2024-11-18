package com.zony.app.domain.vo;

import com.zony.app.domain.Jurisdiction;
import lombok.Data;

/**
 * @Description: 权限手册表单对象
 * @Date
 * @Author ZLK
 */
@Data
public class JurisdictionDocumentFormVo extends AttachFormVo {
    private Jurisdiction formObj;//表单元数据对象
}
