package com.zony.app.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExcelRegularDto implements Serializable {

    private String titleValue = "中国海洋石油有限公司权限手册";//
    private String seqValue = "序号";//
    private String keyPointValue = "^审核要点([\\s：:])*$";//
    private String bussinessNameValue = "业务名称([\\s：:])*";
    private String departValue = "部门([\\s：:])*";

    private int titleRowIndex;//标题行：中国海洋石油有限公司权限手册
    private int titleCellIndex = 0;//标题列
    private int seqCellIndex = 0;//"序号"列
    private int businessNameCellIndex = 0;//业务名称列
    private int keyPointIndex = 0;//审核要点列
    private int groupColCount = 2;//事项组合并列数，（>=）

    private int businessNameRowIndex;//名称行=标题行+1（标题行+1）

    public int getBusinessNameRowIndex() {
        return titleRowIndex + 1;
    }
}
