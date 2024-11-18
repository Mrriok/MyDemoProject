package com.zony.app;

import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileNetFolderTests {

    /**
     * 查询所有子文件夹
     */
    @Test
    public void listSubfolders() {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        List<Folder> folderList = P8Folder.listSubfolders(os);
        folderList.forEach(folder -> System.out.println(folder.get_FolderName()));
    }

    /**
     * 创建文件夹
     */
    @Test
    public void createFolder() {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        Folder newFolder = P8Folder.createFolder(os, P8Folder.getRootFolder(os), "gubin_test1");
        System.out.println(newFolder.get_Id());
    }

    /**
     * 根据路径查询文件夹
     */
    @Test
    public void fetchFolderByPath() {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        Folder folder = P8Folder.fetchFolderByPath(os, "/gubin_test");
        System.out.println(folder.get_FolderName());
    }

    /**
     * 删除文件夹
     */
    @Test
    public void deleteFolder() {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        Folder folder = P8Folder.fetchFolderById(os, "{B182AEAA-7561-4B5F-91F0-10942B09ED50}");
        P8Folder.deleteFolder(folder);
    }

    public static void main(String[] args) {
    }
}

