/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.zony.app.domain.Establishment;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.SystemDocumentQueryCrietria;
import com.zony.app.service.dto.SystemDocumentDto;

import com.zony.app.service.mapstruct.SystemDocumentMapper;
import com.zony.common.config.FileProperties;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/30 -16:05
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "establishmentTest")
public class EstablishmentServiceTest {
    private final SystemDocumentRepository systemDocumentRepository;
    private final SystemDocumentService systemDocumentService;
    private final SystemDocumentMapper systemDocumentMapper;
    private final UserRepository userRepository;
    private final WorkbenchService workbenchService;
    private final FileProperties properties;
    private final RedisUtils redisUtils;
    //private final WorkbenchTestService workbenchTestService;
    public Object queryAll(SystemDocumentQueryCrietria criteria, Pageable pageable) {
        //是否要排序
        //Sort sort = new Sort(Sort.Direction.ASC, "deadline");
        criteria.setInstStatusType(InstStatusTypeEnum.ESTABLISHMENT);
        Page<SystemDocument> page = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    public Object findById(Long id) {
        SystemDocument systemDocument = systemDocumentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SystemDocument.class,"id ",id.toString()));
        SystemDocumentDto systemDocumentDto = systemDocumentMapper.toDto(systemDocument);
        return systemDocumentDto;
    }

    public void create(SystemDocument systemDocument) {
        //可能需要安全校验
        //User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow(() -> new EntityNotFoundException(User.class,"CurrentUserId ",SecurityUtils.getCurrentUserId().toString()));
        systemDocument.setDateOfWriting(new Timestamp(new Date().getTime()));
        systemDocument.setInstStatus(InstStatusEnum.DRAFT);
        systemDocument.setInstStatusType(InstStatusTypeEnum.ESTABLISHMENT);
        systemDocumentRepository.save(systemDocument);
    }

    public void update(SystemDocument systemDocument) {
        //可能需要安全校验
        //if(!SecurityUtils.getCurrentUserId().equals(resources.getOrganizer().getId())){
        //    throw new BadRequestException("无法为更改其他用户创建制度编制记录！");
        //}
        systemDocumentRepository.save(systemDocument);
    }



    public void delete(Set<Long> ids) {
        //权限校验
        ////Set<Long> ids = new HashSet<>();
        //List<Establishment> list =  establishmentRepository.findAllById(ids);
        //for(Establishment item:list){
        //    if(!SecurityUtils.getCurrentUserId().equals(item.getOrganizer().getId())){ //是否进行权限校验,权限校验最好在哪一层
        //        throw new BadRequestException("权限不足！您无法删除制度名称为："+item.getInstName()+" 的记录。");
        //    }
        //    ids.add(item.getId());
        //}
        systemDocumentRepository.deleteByIdIn(ids);
    }

    public void operating(Long id, InstStatusEnum status) {
        SystemDocument systemDocument = systemDocumentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Establishment.class,"id ",id.toString()));
        //if(!SecurityUtils.getCurrentUserId().equals(establishment.getOrganizer().getId())){
        //    throw new BadRequestException("当前用户无权限修改本条记录");
        //}
        //需要安全校验或者校验状态
        WorkbenchVo workbenchVo = new WorkbenchVo();
        workbenchVo.setBusinessType("establishment");
        workbenchVo.setSystemDocument(systemDocument);
        workbenchService.createProcess(workbenchVo);
        systemDocument.setInstStatus(status);
        systemDocumentRepository.save(systemDocument);
    }


}
