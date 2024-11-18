package com.zony.app;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.zony.common.filenet.ce.dao.P8Document;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileNetDocumentTests {
    /**
     * 创建文档
     */
    @Test
    public void createDocument() throws IOException {
        try (InputStream ins = new FileInputStream("f:\\3.doc")) {
            ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ZonyTest1", "111");
            paramMap.put("ZonyTest2", "222");
            P8Document.createDocument(os, "ZonyDocument", "gubin_test1", paramMap, ins);
        }
    }

    /**
     * 文档移到文件夹
     */
    @Test
    public void fileDocument() {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        Document document = P8Document.fetchDocumentById(os, "{CE684AB6-1068-4721-B87E-29851B21C28D}");
        Folder folder = P8Folder.fetchFolderByPath(os, "/notice_annex");
        P8Document.file(document, folder, true);

        //P8Document.unfile(document,folder);
    }

    /**
     * 文档升版
     */
    @Test
    public void upgradeDocument() throws IOException {
        try (InputStream ins = new FileInputStream("f:\\44.pdf")) {
            ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
            Document document = P8Document.fetchDocumentById(os, "{CE684AB6-1068-4721-B87E-29851B21C28D}");
            P8Document.checkOutDocument(os, document);
            Map<String, InputStream> content = new HashMap<>();
            content.put("44", ins);
            P8Document.checkInDocument(os, document, content);
        }
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDocument() {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        Document document = P8Document.fetchDocumentById(os, "{01CE9DD4-8D35-47B9-9CCE-11EC876E7160}");
        P8Document.deleteDocument(document);
    }
}
