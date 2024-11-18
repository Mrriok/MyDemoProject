package com.zony.app.service.dto;

import lombok.Data;

/**
 * 多层制度信息，制度库展示使用
 */
@Data
public class SystemDocMultiLevelDto {

    //基本制度
    private Long basicId;
    private String basicName;
    private String basicCode;
    //管理办法
    private Long regulationId;
    private String regulationName;
    private String regulationCode;
    //操作细则
    private Long operationId;
    private String operationName;
    private String operationCode;
}
