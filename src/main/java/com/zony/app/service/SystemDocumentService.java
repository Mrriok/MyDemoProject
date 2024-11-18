package com.zony.app.service;

import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.SystemDocumentFormVo;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.SystemDocumentQueryCrietria;
import com.zony.app.service.dto.FileAttachDto;

import com.zony.app.service.mapstruct.SystemDocumentMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.SecurityUtils;
import com.zony.common.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 制度文档管理处理类
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Service
@RequiredArgsConstructor
public class SystemDocumentService {
    private final FileUploadService fileUploadService;
    private final SystemDocumentRepository systemDocumentRepository;
    private final SystemDocumentMapper systemDocumentMapper;
    private final UserRepository userRepository;
    private final WorkbenchService workbenchService;

    /**
     * 查看一条制度详细信息
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public SystemDocumentFormVo view(Long id) {
        SystemDocumentFormVo systemDocumentFormVo = new SystemDocumentFormVo();
        SystemDocument systemDocument = this.findById(id);
        systemDocumentFormVo.setFormObj(systemDocument);
        systemDocumentFormVo.setAttachList(fileUploadService.view(systemDocument));
        return systemDocumentFormVo;
    }

    /**
     * 新增一条制度信息
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(SystemDocumentFormVo submitVo) {
        systemDocumentRepository.save(submitVo.getFormObj());
        if (CollectionUtils.isEmpty(submitVo.getAttachList())) {
            throw new RuntimeException("制度附件不能为空。");
        }
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        String folderPath = "/SystemDocument/" + submitVo.getFormObj().getSystemCode();
        fileUploadService.create(os, folderPath, submitVo);
    }

    /**
     * 修改一条制度信息
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SystemDocumentFormVo submitVo) {
        systemDocumentRepository.save(submitVo.getFormObj());
        String folderPath = "/SystemDocument/" + submitVo.getFormObj().getSystemCode();
        fileUploadService.update(submitVo, folderPath);
    }

    /**
     * 删除多条制度
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        for (Long id : ids) {
            SystemDocument systemDocument = this.findById(id);
            fileUploadService.delete(os, systemDocument);
            systemDocumentRepository.delete(systemDocument);
        }
    }

    /**
     * 根据ID查询制度对象
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public SystemDocument findById(Long id) {
        SystemDocument systemDocument = systemDocumentRepository.findById(id).orElseGet(SystemDocument::new);
        ValidationUtil.isNull(systemDocument.getId(), "SystemDocument", "id", id);
        return systemDocument;
    }

    /**
     * 查询制度对象
     *
     * @Author jinweiwei
     * @Date 2020-08-29
     */
    public Object queryAll(SystemDocumentQueryCrietria queryCriteria, Pageable pageable) {
        Page<SystemDocument> page = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(systemDocumentMapper::toDto));
    }
}
