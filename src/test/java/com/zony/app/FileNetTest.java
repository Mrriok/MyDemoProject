/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app;

import com.filenet.api.core.ObjectStore;
import com.zony.common.filenet.ce.dao.P8Document;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/16 -13:46 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileNetTest {

    /**
     * 创建文档
     */
    @Test
    public void createDocument() throws IOException {
        try (InputStream ins = new FileInputStream("C:\\Users\\ASUS\\Desktop\\Opinion.txt")) {
            ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ZonyTest1", "111");
            paramMap.put("ZonyTest2", "222");
            P8Document.createDocument(os, "ZonyTestDocument", "chenhang_test1",paramMap, ins);
        }

    }
}
