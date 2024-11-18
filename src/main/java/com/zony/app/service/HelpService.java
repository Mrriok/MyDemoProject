/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.Help;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.AttachFormVo;
import com.zony.app.domain.vo.HelpDocumentFormVo;

import com.zony.app.repository.HelpRepository;
import com.zony.app.repository.UserRepository;

import com.zony.app.service.criteria.HelpQueryCriteria;
import com.zony.app.service.dto.*;
import com.zony.app.service.mapstruct.HelpMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author Zhijie Yu
 * @date 2020/7/15 -13:32
 */
@Service
@RequiredArgsConstructor
public class HelpService {
    private final HelpRepository helpRepository;
    private final HelpMapper helpMapper;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;


    public Object queryAll(HelpQueryCriteria queryCriteria, Pageable pageable){
        //是否要排序
        //Sort sort = new Sort(Sort.Direction.ASC, "deadline");
        Page<Help> page = helpRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(helpMapper::toDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Help help){
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        if (!SecurityUtils.getCurrentUsername().equals(user.getUsername())){
            throw new BadRequestException("当前用户无权创建发起人为其他用户的记录!");
        }
        help.setAuthor(user);
        helpRepository.save(help);
    }


    @Transactional(rollbackFor = Exception.class)
    public void importHelpExcel(HelpDocumentFormVo helpDocumentFormVo) {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        String folderPath = P8Folder.createFolderIfNotExist(os, "", "help");
        Help newHelpDocument = helpDocumentFormVo.getFormObj();
        //提交人
        newHelpDocument.setAuthor(user);
        newHelpDocument.setName(helpDocumentFormVo.getAttachList().get(0).getFileName());
        helpRepository.save(newHelpDocument);
        fileUploadService.create(os, folderPath, helpDocumentFormVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids){
        //权限校验
        //Set<Long> ids = new HashSet<>();
        //此处权限校验未完成

        List<Help> list = helpRepository.findAllById(ids);
        for(Help item:list){

            ids.add(item.getId());
        }
        helpRepository.deleteAllByIdIn(ids);
    }

    public Help getHelp(Long id) {
        Help help = helpRepository.findById(id).orElseGet(Help::new);
        return help;
    }

    public Object view(Long id) {
        Help help = helpRepository.findById(id).orElseGet(Help::new);
        List<FileAttachDto> fileAttachDtoList = fileUploadService.view(help);
        AttachFormVo attachFormVo = new AttachFormVo();
        attachFormVo.setFormObj(help);
        attachFormVo.setAttachList(fileAttachDtoList);
        return attachFormVo;
    }
}
