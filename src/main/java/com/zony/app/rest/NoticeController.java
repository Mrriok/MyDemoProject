/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.zony.app.domain.Notice;
import com.zony.app.service.NoticeService;
import com.zony.app.service.criteria.NoticeQueryCriteria;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import com.zony.common.filenet.ce.dao.P8Document;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.service.LocalStorageService;
import com.zony.common.service.criteria.LocalStorageQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/8 -16:36
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "公告：公告管理")
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final LocalStorageService localStorageService;
    private static final String ENTITY_NAME = "notice";

    @Log("新增公告")
    @ApiOperation("新增公告")
    @PostMapping(value = "/add")
    @PreAuthorize("@el.check('notice:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Notice resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        noticeService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("删除公告")
    @ApiOperation("删除公告")
    @DeleteMapping(value = "/delete")
    @PreAuthorize("@el.check('notice:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        noticeService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("修改公告")
    @ApiOperation("修改公告")
    @PutMapping(value = "/edit")
    @PreAuthorize("@el.check('notice:edit')")
    public ResponseEntity<Object> update(@Validated(Notice.Update.class) @RequestBody Notice resources){
        noticeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("按照公告id,题目,内容，备注，有效时间查询公告")
    @ApiOperation("按照公告id,题目,内容，备注，有效时间查询公告")
    @GetMapping(value = "/list")
    public ResponseEntity<Object> find(NoticeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(noticeService.queryAll(criteria,pageable), HttpStatus.OK);
    }
    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('notice:list')")
    public void download(HttpServletResponse response, LocalStorageQueryCriteria criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    @ApiOperation("上传文件")
    @PostMapping
    @PreAuthorize("@el.check('notice:add')")
    public ResponseEntity<Object> create(@RequestParam String name, @RequestParam("file") MultipartFile file){
        localStorageService.create(name, file);
        //根据路径查找文件夹，没有则创建
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        Folder folder = P8Folder.fetchFolderByPath(os, "/notice_annex");
        if (folder==null){
            P8Folder.createFolder(os, P8Folder.getRootFolder(os), "notice_annex");
        }
        //FileNet创建文档
        try (InputStream ins = file.getInputStream()) {
            //Map<String, Object> paramMap = new HashMap<>();
            //paramMap.put("ZonyTest1", "111");
            //paramMap.put("ZonyTest2", "222");
            P8Document.createDocument(os, "notice_annex", name, null, ins);//propMap做什么的
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
