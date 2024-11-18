/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.Opinion;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.OpinionFormVo;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.app.repository.OpinionRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.OpinionQueryCriteria;
import com.zony.app.service.dto.FileAttachDto;
import com.zony.app.service.dto.OpinionDto;
import com.zony.app.service.mapstruct.OpinionMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.exception.EntityExistException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -13:32
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "opinion")
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final UserRepository userRepository;
    private final OpinionMapper opinionMapper;
    private final RedisUtils redisUtils;
    private final WorkbenchService workbenchService;
    private final FileUploadService fileUploadService;

    public Object queryAll(OpinionQueryCriteria queryCriteria, Pageable pageable){
        //是否要排序
        //Sort sort = new Sort(Sort.Direction.ASC, "deadline");
        Page<Opinion> page = opinionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(opinionMapper::toDto));
    }
    public Object findById(Long id){
        Opinion opinion = opinionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Opinion.class,"id ",id.toString()));
        OpinionFormVo opinionFormVo = entityToVo(opinion);
        List<FileAttachDto> fileAttachDtoList = fileUploadService.view(opinion);
        if (!CollectionUtils.isEmpty(fileAttachDtoList)){
            opinionFormVo.setAttachList(fileAttachDtoList);
        }else {
            opinionFormVo.setAttachList(new ArrayList<>());
        }
        return opinionFormVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(OpinionFormVo opinionFormVo){
        if (CollectionUtils.isEmpty(opinionFormVo.getAttachList())) {
            throw new RuntimeException("制度附件不能为空。");
        }

        Opinion opinion = voToEntity(opinionFormVo);
        //OpinionDto opinionDto = opinionFormVo.getFormObj();
        if (!SecurityUtils.getCurrentUsername().equals(opinion.getInitUser().getUsername())){
            throw new BadRequestException("当前用户无权创建发起人为其他用户的记录!");
        }
        //opinion.setDeptId(userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername()));

        if(opinion.getStatus() == null){
            opinion.setStatus(ProgressStatusEnum.NO_STARTED);
        }
        Opinion opinionNew = opinionRepository.save(opinion);
        opinionFormVo.setFormObj(opinionNew);
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        String folderPath = "/opinion/" + opinionNew.getInstCode()+opinionNew.getOpinionName();
        fileUploadService.create(os,folderPath,opinionFormVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OpinionFormVo opinionFormVo) {
        Opinion resources = voToEntity(opinionFormVo);
        //是否进行权限校验,权限校验最好在哪一层,校验哪些字段？
        if(!SecurityUtils.getCurrentUsername().equals(resources.getInitUser().getUsername())){
            throw new BadRequestException("当前用户无权限修改本条记录");
        }
        Opinion opinion = opinionRepository.save(resources);
        opinionFormVo.setFormObj(opinion);
        String folderPath = "/opinion/" + opinion.getInstCode()+"_"+opinion.getOpinionName();
        fileUploadService.update(opinionFormVo, folderPath);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids){
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        //权限校验
        //Set<Long> ids = new HashSet<>();
        List<Opinion> list =  opinionRepository.findAllById(ids);
        for(Opinion item:list){
            if(!SecurityUtils.getCurrentUsername().equals(item.getInitUser().getUsername())){ //是否进行权限校验,权限校验最好在哪一层
                throw new BadRequestException("权限不足！您无法删除制度名称为："+item.getInstName()+" 的记录。");
            }
            fileUploadService.delete(os,item);
            ids.add(item.getId());
        }
        opinionRepository.deleteAllByIdIn(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void operating(Long id, ProgressStatusEnum status){
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        Opinion opinion = opinionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Opinion.class,"id ",id.toString()));
        if(!SecurityUtils.getCurrentUsername().equals(opinion.getInitUser().getUsername())){
            throw new BadRequestException("当前用户无权限修改本条记录");
        }
        WorkbenchVo workbenchVo = new WorkbenchVo();
        workbenchVo.setBusinessType("opinion");
        workbenchVo.setOpinion(opinion);
        workbenchService.createProcess(workbenchVo);
        opinion.setStatus(status);
        opinionRepository.save(opinion);
    }

    public void upload(Opinion opinion, List<FileAttachDto> fileAttachDtoList) {
        //FileNet连接对象
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        String parentPath = P8Folder.createFolderIfNotExist(os, "", "opinion");
        String folderName = opinion.getInstCode()+ "_"+ opinion.getInstName(); //
        //检查将要创建的路径是否存在
        Folder folder = P8Folder.fetchFolderByPath(os, parentPath + "\\" + folderName);
        String folderPath = "";
        if(!ObjectUtils.isEmpty(folder)){
            folderPath = parentPath + "\\" + folderName;
        }else {
            //不存在则创建路径
            folderPath = P8Folder.createFolderByPath(os, parentPath, folderName).get_PathName();
        }
        //检验没有存储过的文件添加
        //SystemDocument systemDocument1 = systemDocumentFormVo.getFormObj();
        List<FileAttachDto> fileAttachListOld = fileUploadService.view(opinion);

        Map<String, FileAttachDto> fileAttachFilterMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(fileAttachListOld)){
            fileAttachListOld.forEach(item -> {
                fileAttachFilterMap.put(item.getMd5(), item);
            });
        }
        //验空在controller中进行
        //List<FileAttachDto> fileAttachDtoList = systemDocumentFormVo.getAttachList();
        List<FileAttachDto> saveFileList = fileAttachDtoList.stream().filter(item -> ObjectUtils.isEmpty(fileAttachFilterMap.get(item.getMd5()))).collect(Collectors.toList());
        for (int i = 0; i < saveFileList.size(); i++) {
            fileUploadService.create(os, folderPath, opinion, saveFileList.get(i), i);
        }
    }
    public Opinion voToEntity(OpinionFormVo opinionFormVo){
        //转换基本信息
        OpinionDto opinionDto = opinionFormVo.getFormDtoObj();
        Opinion opinion = opinionMapper.toEntity(opinionDto);
        //转换编制依据，主要应对风险与目标，释义，适用范围
        JsonUtil jsonUtil = JsonUtil.getInstance();
        //List<String> contactPersonIds = new ArrayList<>();
        StringBuilder contactPersonIds = new StringBuilder();
        if(!CollectionUtils.isEmpty(opinionDto.getContactPersonList())){
            opinionDto.getContactPersonList().forEach(item->{
                contactPersonIds.append(",").append(item.getUsername());
            });
            contactPersonIds.append(",");
            opinion.setContactPersonIds(contactPersonIds.toString());
        }else {
            opinion.setContactPersonIds(null);
        }
        return opinion;
    }
    public OpinionFormVo entityToVo(Opinion opinion){
        OpinionFormVo opinionFormVo = new OpinionFormVo();
        OpinionDto opinionDto = opinionMapper.toDto(opinion);

        //转换联系人
        if (!StringUtils.isEmpty(opinion.getContactPersonIds())){
            List<String> usernames = Arrays.asList(opinion.getContactPersonIds().split(","));
            opinionDto.setContactPersonList(userRepository.findByUsernameIn(usernames));
        }else {
            opinionDto.setContactPersonList(new ArrayList<>());
        }
        opinionFormVo.setFormDtoObj(opinionDto);
        return opinionFormVo;
    }
}
