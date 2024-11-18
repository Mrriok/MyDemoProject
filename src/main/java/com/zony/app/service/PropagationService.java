
package com.zony.app.service;


import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.Opinion;
import com.zony.app.domain.Propagation;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.PropagationFormVo;
import com.zony.app.domain.vo.SystemDocumentFormVo;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.app.enums.StatusEnum;
import com.zony.app.repository.PropagtionRepository;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.PropagationQueryCriteria;
import com.zony.app.service.dto.FileAttachDto;
import com.zony.app.service.dto.PropagationDetailDto;
import com.zony.app.service.mapstruct.PropagationMapper;
import com.zony.common.config.FileProperties;
import com.zony.common.domain.vo.EmailVo;
import com.zony.common.exception.BadRequestException;
import com.zony.common.exception.EntityExistException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.service.EmailService;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "propagation")
public class PropagationService {

    private final PropagtionRepository propagtionRepository;
    private final SystemDocumentRepository systemDocumentRepository;
    private final UserRepository userRepository;
    private final PropagationMapper propagationMapper;
    private final FileProperties properties;
    private final FileUploadService fileUploadService;
    private final EmailService emailService;
    private final RedisUtils redisUtils;


    public Object queryAll(PropagationQueryCriteria criteria, Pageable pageable) {
        Page<Propagation> page = propagtionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }


    @Transactional(rollbackFor = Exception.class)
    public void create(PropagationFormVo propagationFormVo) throws IOException {
        if (CollectionUtils.isEmpty(propagationFormVo.getAttachList())) {
            throw new RuntimeException("制度附件不能为空。");
        }
        Propagation propagation = voToEntity(propagationFormVo);
        //可能需要安全校验
        //User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow(() -> new EntityNotFoundException(User.class,"CurrentUserId ",SecurityUtils.getCurrentUserId().toString()));
        propagation.setLaunchTime(new Timestamp(new Date().getTime()));
        propagation.setStatus(ProgressStatusEnum.NO_STARTED);
        Propagation propagationNew = propagtionRepository.save(propagation);
        propagationFormVo.setFormObj(propagationNew);
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        String folderPath = "/propagation/" + propagationNew.getPropagationName();
        fileUploadService.create(os,folderPath,propagationFormVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PropagationFormVo propagationFormVo) throws IOException {
        Propagation propagation = voToEntity(propagationFormVo);
        //可能需要安全校验
        //if(!SecurityUtils.getCurrentUserId().equals(resources.getOrganizer().getId())){
        //    throw new BadRequestException("无法为更改其他用户创建制度编制记录！");
        //}
        Propagation propagationNew = propagtionRepository.save(propagation);
        String folderPath = "/propagation/" + propagationNew.getPropagationName();
        propagationFormVo.setFormObj(propagationNew);
        fileUploadService.update(propagationFormVo, folderPath);
        //if(!CollectionUtils.isEmpty(propagationFormVo.getAttachList())){
        //    uploadAttachments(propagationNew,propagationFormVo.getAttachList());
        //}

    }

    @Transactional(rollbackFor = Exception.class)
    public void launch(Long id) {
        Propagation propagation = propagtionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Propagation.class, "id ", id.toString()));
        if (!ProgressStatusEnum.NO_STARTED.getCode().equals(propagation.getStatus().getCode())){
            throw new BadRequestException("该条记录状态不允许被修改！");
        }
        propagation.setStatus(ProgressStatusEnum.PROGRESS);
        propagation.setLaunchTime(new Timestamp(new Date().getTime()));
        propagtionRepository.save(propagation);
        remindUsers(propagation);
    }

    private void remindUsers(Propagation propagation){
        EmailVo emailVo = new EmailVo();
        Map<String,Object> paramMap = new HashMap<>();
        emailVo.setTitle(propagation.getPropagationName());
        User drafter = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        JsonUtil jsonUtil = JsonUtil.getInstance();
        if(StringUtils.isEmpty(propagation.getContactPersonIds())){
            throw new BadRequestException("宣贯对象联系人为空，请添加联系人后发起");
        }
        List<String> usernames = (List<String>) jsonUtil.json2obj(propagation.getContactPersonIds(),List.class);
        //User drafter = userRepository.findById(workflowCommon.getStartUsername()).orElseGet(User::new);
        //paramMap.put("handlerName"," 当前用户（上一步处理人）："+handler.getUsername());
        paramMap.put("drafterName",drafter.getNickName());
        paramMap.put("propagationName",propagation.getPropagationName());
        emailVo.setParam(paramMap);
        List<String> address = new ArrayList<>();
        List<User> userList = userRepository.findByUsernameIn(usernames);
        if (!CollectionUtils.isEmpty(userList)){
            userList.forEach(item->{
                address.add(item.getEmail());
            });
        }
        emailVo.setAddress(address);
        emailService.sendProcessAgentEmail(emailVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        //权限校验
        //Set<Long> ids = new HashSet<>();
        List<Propagation> list =  propagtionRepository.findAllById(ids);
        for(Propagation item:list){
            if(!SecurityUtils.getCurrentUsername().equals(item.getInitUser().getUsername())){ //是否进行权限校验,权限校验最好在哪一层
                throw new BadRequestException("权限不足！您无法删除制度名称为："+item.getInitUser().getUsername()+" 的记录。");
            }
            fileUploadService.delete(os,item);
            ids.add(item.getId());
        }
        propagtionRepository.deleteAllByIdIn(ids);
    }


    public PropagationFormVo findById(Long id) {
        Propagation propagation = propagtionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Propagation.class, "id ", id.toString()));
        PropagationFormVo propagationFormVo = entityToVo(propagation);
        propagationFormVo.setFormObj(propagation);
        //SystemDocumentDto systemDocumentDto = systemDocumentMapper.toDto(systemDocument);
        List<FileAttachDto> fileAttachDtoList = fileUploadService.view(propagation);
        propagationFormVo.setAttachList(fileAttachDtoList);
        return propagationFormVo;
    }
    public void uploadAttachments(Propagation propagation, List<FileAttachDto> fileAttachDtoList) throws IOException {
        //FileNet连接对象
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        //controller验空
        String parentPath = P8Folder.createFolderIfNotExist(os, "", "establishment");
        String folderName = propagation.getId()+ "_" + propagation.getPropagationName(); //
        //检查将要创建的路径是否存在
        Folder folder = P8Folder.fetchFolderByPath(os, parentPath + "\\" + folderName);
        String folderPath = "";
        if (!ObjectUtils.isEmpty(folder)) {
            folderPath = parentPath + "\\" + folderName;
        } else {
            //不存在则创建路径
            folderPath = P8Folder.createFolderByPath(os, parentPath, folderName).get_PathName();
        }
        //检验没有存储过的文件添加
        //SystemDocument systemDocument1 = systemDocumentFormVo.getFormObj();
        List<FileAttachDto> fileAttachListOld = fileUploadService.view(propagation);

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
            fileUploadService.create(os, folderPath, propagation, saveFileList.get(i), i);
        }
    }

    public Propagation voToEntity(PropagationFormVo propagationFormVo){
        //转换基本信息
        PropagationDetailDto propagationDetailDto = propagationFormVo.getFormDtoObj();
        Propagation propagation = propagationMapper.toEntity(propagationDetailDto);
        List<SystemDocument> systemDocumentList = propagationDetailDto.getSystemDocumentList();
        List<User> contactPersonList = propagationDetailDto.getContactPersonList();
        Set<Long> systemDocumentIds = new HashSet<>();
        List<String> contactPersonIds = new ArrayList<>();
        JsonUtil jsonUtil = JsonUtil.getInstance();
        if(!CollectionUtils.isEmpty(systemDocumentList)){
            systemDocumentIds = systemDocumentList.stream().map(SystemDocument::getId)
                    .collect(Collectors.toSet());
            propagation.setSystemDocumentIds(jsonUtil.obj2json(systemDocumentIds));
        }
        if (!CollectionUtils.isEmpty(contactPersonList)){
            contactPersonIds = contactPersonList.stream().map(User::getUsername)
                    .distinct().collect(Collectors.toList());
            propagation.setContactPersonIds(jsonUtil.obj2json(contactPersonIds));
        }

        return propagation;
    }
    public PropagationFormVo entityToVo(Propagation propagation){
        PropagationFormVo propagationFormVo = new PropagationFormVo();
        PropagationDetailDto propagationDetailDto = propagationMapper.toDto(propagation);
        JsonUtil jsonUtil = JsonUtil.getInstance();
        if(!StringUtils.isEmpty(propagation.getSystemDocumentIds())){
            Set<Integer> systemDocumentIntegerIds = (Set<Integer>) jsonUtil.json2obj(propagation.getSystemDocumentIds(),Set.class);
            List<Long> systemDocumentLongIds = systemDocumentIntegerIds.stream().map(i->Long.valueOf(i.toString())).collect(Collectors.toList());
            propagationDetailDto.setSystemDocumentList(systemDocumentRepository.findAllById(systemDocumentLongIds));
        }else {
            propagationDetailDto.setSystemDocumentList(new ArrayList<>());
        }

        if (!StringUtils.isEmpty(propagation.getContactPersonIds())){
            List<String> usernames = (List<String>) jsonUtil.json2obj(propagation.getContactPersonIds(),List.class);
            propagationDetailDto.setContactPersonList(userRepository.findByUsernameIn(usernames));
        }else {
            propagationDetailDto.setContactPersonList(new ArrayList<>());
        }
        propagationFormVo.setFormDtoObj(propagationDetailDto);
        return propagationFormVo;
    }

    //时间 上传人 上传时间 文件名 文件ID
    public List<FileAttachDto> viewAttachments(Long id) {
        Propagation propagation = propagtionRepository.findById(id).orElseGet(Propagation::new);
        return fileUploadService.view(propagation);
    }

}
