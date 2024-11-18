package com.zony.app.service.dto;

import lombok.Data;

/**
 * 制度版本信息，制度库导出使用
 */
@Data
public class SystemDocExportVersionDto {

    private String systemCode;//制度编码
    private String systemTitle;//制度名称
    private String deptName;//编写部门  ->deptId
    private String version;//当前版本 ->version+modifyCode
    private String publishSymbol;//发布文号
    private String publishDateStr;//发布日期
    private String effectiveDateStr;//生效期
}
