/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.Establishment;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.vo.SystemDocumentFormVo;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.InstPropertyEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.SystemDocumentQueryCrietria;
import com.zony.app.service.dto.FileAttachDto;
import com.zony.app.service.dto.SystemDocumentAttachDto;
import com.zony.app.service.mapstruct.SystemDocumentMapper;
import com.zony.common.config.FileProperties;
import com.zony.common.exception.BadRequestException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 *
 * @author MrriokChen
 * @version v1.0
 * @date 2020/7/30 -16:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "establishment")
public class EstablishmentService {
    private final SystemDocumentRepository systemDocumentRepository;
    private final SystemDocumentService systemDocumentService;
    private final SystemDocumentMapper systemDocumentMapper;
    private final UserRepository userRepository;
    private final WorkbenchService workbenchService;
    private final FileUploadService fileUploadService;
    private final FileProperties properties;
    private final RedisUtils redisUtils;

    //private final WorkbenchTestService workbenchTestService;
    public Object queryAll(SystemDocumentQueryCrietria criteria, Pageable pageable) {
        //是否要排序
        //Sort sort = new Sort(Sort.Direction.ASC, "deadline");
        criteria.setInstStatusType(InstStatusTypeEnum.ESTABLISHMENT);
        Page<SystemDocument> page = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    public Object findById(Long id) {
        SystemDocumentFormVo systemDocumentFormVo = new SystemDocumentFormVo();
        SystemDocument systemDocument = systemDocumentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SystemDocument.class, "id ", id.toString()));
        systemDocumentFormVo.setFormDtoObj(systemDocumentMapper.toDto(systemDocument));
        List<FileAttachDto> fileAttachDtoList = fileUploadService.view(systemDocument);
        systemDocumentFormVo.setAttachList(fileAttachDtoList);
        return systemDocumentFormVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(SystemDocumentFormVo systemDocumentFormVo) throws IOException {
        if (CollectionUtils.isEmpty(systemDocumentFormVo.getAttachList())) {
            throw new RuntimeException("制度附件不能为空。");
        }
        SystemDocument systemDocument = systemDocumentFormVo.getFormObj();
        //可能需要安全校验
        //User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow(() -> new EntityNotFoundException(User.class,"CurrentUserId ",SecurityUtils.getCurrentUserId().toString()));
        if (InstPropertyEnum.UPDATE.getCode().equals(systemDocument.getInstProperty().getCode())){
            SystemDocument systemDocumentOld = systemDocumentRepository.findBySystemCodeAndCurrentVersionFlag(systemDocument.getSystemCode(),true);
            if (systemDocumentOld.getModifyCode() != null &&
                    Integer.valueOf(systemDocumentOld.getModifyCode()) < 5){
                systemDocument.setModifyCode(String.valueOf(Integer.valueOf(systemDocumentOld.getModifyCode())+1));
            }else {
                systemDocument.setVersion(String.valueOf((char)(systemDocument.getVersion().charAt(0) + 1)));
                systemDocument.setModifyCode("0");
            }
        }else {
            systemDocument.setVersion("A");
            systemDocument.setModifyCode("0");
        }
        systemDocument.setDateOfWriting(new Timestamp(new Date().getTime()));
        systemDocument.setInstStatus(InstStatusEnum.DRAFT);
        systemDocument.setInstStatusType(InstStatusTypeEnum.ESTABLISHMENT);
        systemDocument.setCurrentVersionFlag(false);
        //systemDocument.setDeptId();
        //systemDocument.setDeptId(userRepository.findDeptIdByUsername(systemDocument.getInitUser()));
        systemDocumentRepository.save(systemDocument);
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        String folderPath = "/establishment/" + systemDocument.getSystemCode() + "_" + systemDocument.getSystemTitle();
        fileUploadService.create(os, folderPath, systemDocumentFormVo);
    }

    public void update(SystemDocumentFormVo systemDocumentFormVo) {
        SystemDocument systemDocument = systemDocumentRepository.findById(systemDocumentFormVo.getFormDtoObj().getId()).orElseGet(SystemDocument::new);
        //可能需要安全校验
        if (!SecurityUtils.getCurrentUsername().equals(systemDocument.getCreateBy())) {
            throw new BadRequestException("权限不足，您无法为更新制度编制名称为" + systemDocument.getSystemTitle() + "的记录！");
        }
        systemDocument = systemDocumentFormVo.getFormObj();
        systemDocumentRepository.save(systemDocument);
        String folderPath = "/establishment/" + systemDocument.getSystemCode() + systemDocument.getSystemTitle();
        fileUploadService.update(systemDocumentFormVo, folderPath);
    }

    //时间 上传人 上传时间 文件名 文件ID
    public List<FileAttachDto> viewAttachments(Long id) {
        SystemDocument systemDocument = systemDocumentRepository.findById(id).orElseGet(SystemDocument::new);
        return fileUploadService.view(systemDocument);
    }

    public void delete(Set<Long> ids) {
        String username = SecurityUtils.getCurrentUsername();
        //权限校验
        ////Set<Long> ids = new HashSet<>();
        //List<Establishment> list =  establishmentRepository.findAllById(ids);
        //for(Establishment item:list){
        //    if(!SecurityUtils.getCurrentUserId().equals(item.getOrganizer().getId())){ //是否进行权限校验,权限校验最好在哪一层
        //        throw new BadRequestException("权限不足！您无法删除制度名称为："+item.getInstName()+" 的记录。");
        //    }
        //    ids.add(item.getId());
        //}
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        for (Long id : ids) {
            SystemDocument systemDocument = systemDocumentRepository.findById(id).orElseGet(SystemDocument::new);
            if (!username.equals(systemDocument.getCreateBy())) {
                throw new BadRequestException("权限不足，您无法为删除制度编制名称为" + systemDocument.getSystemTitle() + "的记录！");
            }
            fileUploadService.delete(os, systemDocument);
            systemDocumentRepository.delete(systemDocument);
        }
        //systemDocumentRepository.deleteByIdIn(ids);
    }

    public void operating(Long id, InstStatusEnum instStatus) {
        //User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        SystemDocument systemDocument = systemDocumentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Establishment.class, "id ", id.toString()));
        //if(!SecurityUtils.getCurrentUserId().equals(establishment.getOrganizer().getId())){
        //    throw new BadRequestException("当前用户无权限修改本条记录");
        //}
        //需要安全校验或者校验状态
        if (!SecurityUtils.getCurrentUsername().equals(systemDocument.getCreateBy())) {
            throw new BadRequestException("权限不足，您无法为发起制度编制名称为" + systemDocument.getSystemTitle() + "的记录！");
        }
        WorkbenchVo workbenchVo = new WorkbenchVo();
        workbenchVo.setBusinessType("establishment");
        workbenchVo.setSystemDocument(systemDocument);
        workbenchService.createProcess(workbenchVo);
        systemDocument.setInstStatus(instStatus);
        systemDocumentRepository.save(systemDocument);
    }

    //public void operatingTest(Long id, InstStatusEnum status) {
    //    User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow(() -> new EntityNotFoundException(User.class,"CurrentUserId ",SecurityUtils.getCurrentUserId().toString()));
    //    Establishment establishment = establishmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Establishment.class,"id ",id.toString()));
    //    if(!SecurityUtils.getCurrentUserId().equals(establishment.getOrganizer().getId())){
    //        throw new BadRequestException("当前用户无权限修改本条记录");
    //    }
    //    WorkbenchTestVo workbenchTestVo = new WorkbenchTestVo();
    //    workbenchTestVo.setBusinessType("establishment");
    //    workbenchTestVo.setEstablishment(establishment);
    //    workbenchTestService.createProcess(workbenchTestVo);
    //    establishment.setStatus(status);
    //    establishmentRepository.save(establishment);
    //}

    /**
     * 生成制度正文
     *
     * @Author Michael
     * @Date 2020-09-09
     */
    public String createText(SystemDocument systemDocument) throws Exception {
        //临时设置，待公司名称字段加入后取消
        systemDocument.setCompanyName("公司名称test");
        if (StringUtils.isNotEmpty(systemDocument.getDocApplyRange())) {
            systemDocument.setDocApplyRange(systemDocument.getDocApplyRange().replace("#", "；"));
        }
        if (StringUtils.isNotEmpty(systemDocument.getDocAccording())) {
            systemDocument.setDocAccording(systemDocument.getDocAccording().replace("#", ""));
        }
//        if (StringUtils.isNotEmpty(systemDocument.getDocReplyRisk())) {
//            systemDocument.setDocReplyRisk(systemDocument.getDocReplyRisk().replace("#", ""));
//        }
        if (StringUtils.isNotEmpty(systemDocument.getDocExplain())) {
            systemDocument.setDocExplain(systemDocument.getDocExplain().replace("#", ""));
        }
        String templateName = "";
        File tmpFile = null;
        //获取制度正文模版
        if ("baseInst".equals(systemDocument.getInstLevel().getCode())) {
            templateName = "基本制度.xml";
        } else if ("managementMethod".equals(systemDocument.getInstLevel().getCode())) {
            templateName = "管理办法.xml";
        } else if ("operatingRule".equals(systemDocument.getInstLevel().getCode())) {
            templateName = "操作细则.xml";
        }
        //制度正文模版存放目录
        String templateFolder = ResourceUtils.getURL("classpath:").getPath() + "\\public\\systemDoc";
        //生成的临时文件
        String tempFilePath = ResourceUtils.getURL("classpath:").getPath() + "\\public\\tempFile\\" + systemDocument.getSystemTitle() + ".doc";
        WordUtil.writeWord(templateFolder, templateName, tempFilePath, systemDocument);
        return systemDocument.getSystemTitle() + ".doc";
    }

    //从制度正文导出元数据
    public SystemDocument importText(String filePath, String type) throws Exception {
        String path = properties.getPath().getPath() + File.separatorChar + "upload-file" + File.separatorChar + "file";
        File file = new File(path, String.format("%s_tmp", filePath));
        SystemDocument systemDocument = new SystemDocument();
        try (InputStream ins = new FileInputStream(file)) {
            systemDocument = WordUtil.importWord(ins, type);
        }
        return systemDocument;
    }

}
