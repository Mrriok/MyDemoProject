package com.zony.app.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 制度主信息，制度库导出使用
 */
@Data
public class SystemDocExportMainDto {

    private String systemCode;//制度编号
    private SystemDocExportMainDto parent;//上级制度
    private List<SystemDocExportMainDto> subLevelList = new ArrayList<>();//下级制度集合
    private List<SystemDocExportVersionDto> versionList = new ArrayList<>();//版本集合
    private int startRowIndex;//开始行索引
    private int rowCount;//所占行数
    private int levelNo;//第几层级
}
