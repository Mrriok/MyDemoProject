package com.zony.app.service;

import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.Baselibrary;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.AttachFormVo;
import com.zony.app.domain.vo.BaselibraryFormVo;
import com.zony.app.repository.BaselibraryRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.BaselibraryQueryCriteria;
import com.zony.app.service.dto.FileAttachDto;
import com.zony.app.service.mapstruct.BaselibraryMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "baselirary")
public class BaselibraryService {

    private final BaselibraryRepository baseLibraryRepository;
    private final BaselibraryMapper baseLibraryMapper;
    private final FileUploadService fileUploadService;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> list) {
        Set<Long> ids = new HashSet<>();
        baseLibraryRepository.deleteAllByIdIn(list);
    }

    public Object queryAll(BaselibraryQueryCriteria queryCriteria, Pageable pageable) {
        Page<Baselibrary> page = baseLibraryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(baseLibraryMapper::toDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Baselibrary resources) {
        if (!ObjectUtils.isEmpty(baseLibraryRepository.findByBaselibraryName(resources.getBaselibraryName()))){
            throw new BadRequestException("编制依据名称不能重复~");
        }
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        //上传则设置上传时间
        resources.setLaunchTime(new Timestamp(new Date().getTime()));
        resources.setUploaderId(user.getUsername());
        resources.setUploaderName(user.getNickName());
        baseLibraryRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void upload(List<FileAttachDto> resources) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        //FileNet连接对象
        //ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        //获取存储路径
        //String parentPath = P8Folder.createFolderIfNotExist(os, "", "baselibrary");
        //Integer i = 1;
        //List<Baselibrary> baselibraryList = new ArrayList<>();
        for(FileAttachDto item:resources){
            Baselibrary baselibrary = new Baselibrary();
            baselibrary.setLaunchTime(new Timestamp(new Date().getTime()));
            baselibrary.setBaselibraryName(item.getFileName().substring(0,item.getFileName().lastIndexOf('.')));
            baselibrary.setUploaderId(user.getUsername());
            baselibrary.setUploaderName(user.getNickName());
            Map<String,Object> otherInfoMap = item.getOtherInfo();
            baselibrary.setFlag(MapUtils.getBoolean(otherInfoMap,"flag"));
            //baselibrary.setSystemId(MapUtils.getLong(otherInfoMap,"systemId"));
            //保存附件之前需要检验系统中是否有同名的文件
            if (!ObjectUtils.isEmpty(baseLibraryRepository.findByBaselibraryName(baselibrary.getBaselibraryName()))){
                throw new BadRequestException("编制依据名称不能重复~");
            }
            Baselibrary baselibraryNew = baseLibraryRepository.save(baselibrary);
            BaselibraryFormVo baselibraryFormVo = new BaselibraryFormVo();
            baselibraryFormVo.setFormObj(baselibraryNew);
            List<FileAttachDto> fileAttachDtoList = new ArrayList<>();
            fileAttachDtoList.add(item);
            baselibraryFormVo.setAttachList(fileAttachDtoList);
            ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
            String folderPath = "/baseLibrary";
            fileUploadService.create(os, folderPath, baselibraryFormVo);
        }

    }

    public void update(Baselibrary resources) {
        Baselibrary baselibrary = baseLibraryRepository.findById(resources.getId()).orElseGet(Baselibrary::new);
        if (SecurityUtils.getCurrentUsername().equals(baselibrary.getUploaderId())){
            throw new BadRequestException("您无法修改其他上传人的编制依据记录！");
        }
        baseLibraryRepository.save(resources);
    }

    public Object view(Long id) {
        Baselibrary baselibrary = baseLibraryRepository.findById(id).orElseGet(Baselibrary::new);
        List<FileAttachDto> fileAttachDtoList = fileUploadService.view(baselibrary);
        BaselibraryFormVo baselibraryFormVo = new BaselibraryFormVo();
        baselibraryFormVo.setFormObj(baselibrary);
        baselibraryFormVo.setAttachList(fileAttachDtoList);
        return baselibraryFormVo;
    }
}
