package com.zony.app.service;

import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.*;
import com.zony.app.domain.vo.JurisdictionDocumentFormVo;
import com.zony.app.repository.*;
import com.zony.app.service.criteria.JurisdictionQueryCriteria;
import com.zony.app.service.dto.*;
import com.zony.app.service.mapstruct.JurisdictionMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "jurisdiction")
public class JurisdictionService {

    private final JurisdictionReposity jurisdictionReposity;
    private final JurisdictionMapper jurisdictionMapper;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final DictDetailService dictDetailService;
    private final DictDetailRepository dictDetailRepository;
    private final DictRepository dictRepository;
    private final AnalysisService analysisService;
    private final AnalysisTableRepository analysisTableRepository;

    public Object queryAll(JurisdictionQueryCriteria queryCriteria, Pageable pageable) {
        Page<Jurisdiction> page = jurisdictionReposity.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(jurisdictionMapper::toDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Jurisdiction resources) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        resources.setUpdatePerson(user);
        //上传则设置更新时间
        resources.setLaunchTime(new Timestamp(System.currentTimeMillis()));
        //上传自动更新版本号
        resources.setSequence("v" + (jurisdictionReposity.countByCompanyName(resources.getCompanyName()) * 1.0 + 1.0));
        jurisdictionReposity.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void importJurisdictionExcel(JurisdictionDocumentFormVo jurisdictionDocumentFormVo) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        Jurisdiction newJurisdictionDocument = jurisdictionDocumentFormVo.getFormObj();
        //提交人
        newJurisdictionDocument.setUpdatePerson(user);
        //版本号
        newJurisdictionDocument.setSequence(getJurisdictionVersion(newJurisdictionDocument.getCompanyName()));
        if (CollectionUtils.isEmpty(jurisdictionDocumentFormVo.getAttachList())) {
            throw new BadRequestException("权限手册附件不能为空。");
        }
        newJurisdictionDocument.setJurisdictionName(jurisdictionDocumentFormVo.getAttachList().get(0).getFileName());
        //保存权限手册主体
        jurisdictionReposity.save(newJurisdictionDocument);
        //FileNet连接对象
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        //CE-权限手册保存路径
        String folderPath = "/JurisdictionDocument";
        for (int i = 0; i < jurisdictionDocumentFormVo.getAttachList().size(); i++) {
            FileAttachDto fileAttachDto = jurisdictionDocumentFormVo.getAttachList().get(i);
            FileAttach fileAttach = fileUploadService.create(os, folderPath, newJurisdictionDocument, fileAttachDto, i + 1);
            newJurisdictionDocument.setFileAttach(fileAttach);
            jurisdictionReposity.save(newJurisdictionDocument);
            //从缓存目录获取文件进行解析
            File tmpFile = new File(fileUploadService.getUploadTempPath(), fileAttachDto.getMd5() + "_" + fileAttachDto.getFileName());
            //解析Excel
            List<AnalysisTable> analysisTableList = null;
            try {
                analysisTableList = analysisService.readFromExcel(tmpFile.getAbsolutePath());
                if (ObjectUtils.isEmpty(analysisTableList)) {
                    throw new BadRequestException("Excel解析数据为空，请检查数据");
                }
                analysisTableList.forEach(analysisTable -> {
                    analysisTable.setJurisdiction(newJurisdictionDocument);
                });
                //保存所有解析数据
                analysisTableRepository.saveAll(analysisTableList);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BadRequestException("Excel解析失败:" + e.getMessage());
            }
        }
    }

    /**
     * @param label 公司名称
     * @return String 返回版本号字符串，版本号格式（v1.0、v2.0）
     * @Description: 权限手册主体获取版本号，基于公司名称自增，在数据字典中配置
     * @Date 2020/9/9 10:57
     * @Author ZLK
     */
    public String getJurisdictionVersion(String label) {
        String version = "1";
        DictDetail dictDetails = dictDetailRepository.findByDictNameAndLabel("jurisdiction_version", label);
        if (!ObjectUtils.isEmpty(dictDetails)) {
            version = String.valueOf(Integer.parseInt(dictDetails.getValue()) + 1);
            dictDetails.setValue(version);
            //更新
            dictDetailService.update(dictDetails);
        } else {
            DictDetail dictDetail = new DictDetail();
            dictDetail.setDict(dictRepository.findByName("jurisdiction_version"));
            // 公司名称
            dictDetail.setLabel(label);
            dictDetail.setValue(version);
            //创建
            dictDetailService.create(dictDetail);
        }
        version = "v" + version + ".0";
        return version;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        //FileNet连接对象
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        //删除权限手册，先删表数据，最后删CE文件，以防删除数据库时报错可回滚
        for (Long id : ids) {
            //根据ID查询权限手册主体
            Jurisdiction jurisdiction = jurisdictionReposity.findById(id).orElseGet(Jurisdiction::new);
            //删除权限手册主体表中数据
            jurisdictionReposity.deleteById(id);
            //删除解析数据
            Set<AnalysisTable> tableSet = jurisdiction.getAnalysisTableSet();
            tableSet.forEach(analysisTableRepository::delete);
            //删除CE中的附件
            fileUploadService.delete(os, jurisdiction);
        }
    }
}
