
package com.zony.app.service;


import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.vo.AttachmentVo;


import com.zony.common.filenet.ce.dao.P8Document;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import com.zony.common.config.FileProperties;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "propagation")
public class DocumentService {

    private final FileProperties properties;
    private final RedisUtils redisUtils;


//    public AttachmentVo uploadAttachments(Map<String, Object> map, InputStream in, String fileName){
//        AuthenticatedObjectStore auos = AuthenticatedObjectStore.createDefault();
//        ObjectStore os=auos.getObjectStore();
//        String documentTitle = fileName;
//        String folderPath= (String) map.get("folderPath");
//
//        // 创建文档前进行属性封装
//        Map<String, InputStream> content = new HashMap<String, InputStream>();
//        content.put(fileName, in);
//        // 创建文档
//        Document document = P8Document.createDocument(os, "AttachmentDoc", documentTitle, map ,content);
//        Folder folder = P8Folder.fetchFolderByPath(os, folderPath);
//        P8Document.file(document, folder, true);
//        document.save(RefreshMode.REFRESH);
//        AttachmentVo attachmentVo=new AttachmentVo();
//        attachmentVo.setDocId(document.get_Id().toString());
//        attachmentVo.setFileName(fileName);
//        return  attachmentVo;
//    }

    public AttachmentVo uploadAttachments(MultipartFile file) throws IOException {
        AuthenticatedObjectStore auos = AuthenticatedObjectStore.createDefault();
        ObjectStore os=auos.getObjectStore();
        //文件流
        InputStream in = file.getInputStream();
        //文件名
        String documentTitle = file.getOriginalFilename();
        //属性
        Map<String,Object> propMap = new HashMap<String, Object>();

        File savefile = FileUtil.upload(file, properties.getPath().getPath());
        // 创建文档前进行属性封装
        Map<String, InputStream> content = new HashMap<String, InputStream>();
        content.put(documentTitle,in );
        // 创建文档
        Document document = P8Document.createDocument(os, "AttachmentDoc", documentTitle, propMap ,content);


        Folder folder = P8Folder.fetchFolderByPath(os, "/Attachment");
        P8Document.file(document, folder, true);
        document.save(RefreshMode.REFRESH);
        AttachmentVo attachmentVo=new AttachmentVo();
        attachmentVo.setDocId(document.get_Id().toString());
        attachmentVo.setFileName(documentTitle);
        return attachmentVo;
    }
    //时间 上传人 上传时间 文件名 文件ID




}
