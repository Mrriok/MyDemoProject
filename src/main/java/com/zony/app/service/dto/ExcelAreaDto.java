package com.zony.app.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExcelAreaDto implements Serializable {

    private final String cellValue;
    private final int rowIndex;
    private final int rowCount;
    private final int colCount;
}
