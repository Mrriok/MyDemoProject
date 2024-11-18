package com.zony.app;

import com.zony.app.service.dto.SystemDocumentSmallDto;
import com.zony.common.utils.JsonUtil;

import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        //System.out.println("1019-SP-01 中国海洋石油有限公司规划管理制度.docx".matches(".+_(\\d)+\\.(doc|docx)"));


        List<SystemDocumentSmallDto> list = JsonUtil.getInstance().json2list("[{\"systemTitle\":\"\",\"systemCode\":\"bbb\"},{\"systemTitle\":null,\"systemCode\":\"ccc\"}]", SystemDocumentSmallDto.class);
        System.out.println(list.size());
    }
}
