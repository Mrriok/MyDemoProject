package com.zony.app.service.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ExcelCombineCellDto implements Serializable {

    private boolean combineFlag = false;
    private int rowCount = 0;
    private int colCount = 0;
}
