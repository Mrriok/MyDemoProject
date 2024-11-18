package com.zony.app.service;

import com.zony.app.domain.FileAttach;
import com.zony.app.domain.Regulationsort;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.SystemDocumentDetailVo;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.repository.FileAttachRepository;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.RegulationlibraryQueryCriteria;
import com.zony.app.service.dto.SystemDocMultiLevelDto;
import com.zony.app.service.dto.SystemDocSubListDto;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 制度库查询管理
 *
 * @Author gubin
 * @Date 2020-09-16
 */
@Service
@RequiredArgsConstructor
public class RegulationlibraryService {

    private final SystemDocumentRepository systemDocumentRepository;
    private final SystemDocumentService systemDocumentService;
    private final FileUploadService fileUploadService;
    private final FileAttachRepository fileAttachRepository;
    private final UserRepository userRepository;
    private final WorkbenchService workbenchService;

    /**
     * 基本制度查询条件
     *
     * @Author gubin
     * @Date 2020-09-16
     */
    public RegulationlibraryQueryCriteria createBasicQueryCriteria(RegulationlibraryQueryCriteria queryCriteria) {
        RegulationlibraryQueryCriteria basicQueryCriteria = new RegulationlibraryQueryCriteria();
        BeanUtils.copyProperties(queryCriteria, basicQueryCriteria);
        if (basicQueryCriteria.getSystemCode() == null) {
            basicQueryCriteria.setSystemCode("");
        }
        if (queryCriteria.getRegulationsortId() != null) {
            Regulationsort regulationsort = new Regulationsort();
            regulationsort.setId(queryCriteria.getRegulationsortId());
            basicQueryCriteria.setRegulationsort(regulationsort);
        }
        basicQueryCriteria.setCurrentVersionFlag(true);
        basicQueryCriteria.setInstStatusType(InstStatusTypeEnum.LIBRARY);
        basicQueryCriteria.setInstStatus(InstStatusEnum.ACCEPTED);
        basicQueryCriteria.setInstLevel(InstLevelEnum.BASE_INST);
        return basicQueryCriteria;
    }

    /**
     * 管理办法查询条件
     *
     * @Author gubin
     * @Date 2020-09-16
     */
    public RegulationlibraryQueryCriteria createRegulationQueryCriteria(RegulationlibraryQueryCriteria queryCriteria, SystemDocument basicObj) {
        RegulationlibraryQueryCriteria regulationQueryCriteria = new RegulationlibraryQueryCriteria();
        BeanUtils.copyProperties(queryCriteria, regulationQueryCriteria);
        regulationQueryCriteria.setSystemCode(basicObj.getSystemCode());
        if (queryCriteria.getRegulationsortId() != null) {
            Regulationsort regulationsort = new Regulationsort();
            regulationsort.setId(queryCriteria.getRegulationsortId());
            regulationQueryCriteria.setRegulationsort(regulationsort);
        }
        regulationQueryCriteria.setCurrentVersionFlag(true);
        regulationQueryCriteria.setInstStatusType(InstStatusTypeEnum.LIBRARY);
        regulationQueryCriteria.setInstStatus(InstStatusEnum.ACCEPTED);
        regulationQueryCriteria.setInstLevel(InstLevelEnum.MANAGEMENT_METHOD);
        return regulationQueryCriteria;
    }

    /**
     * 操作细则查询条件
     *
     * @Author gubin
     * @Date 2020-09-16
     */
    public RegulationlibraryQueryCriteria createOperationQueryCriteria(RegulationlibraryQueryCriteria queryCriteria, SystemDocument regulationObj) {
        RegulationlibraryQueryCriteria operationQueryCriteria = new RegulationlibraryQueryCriteria();
        BeanUtils.copyProperties(queryCriteria, operationQueryCriteria);
        operationQueryCriteria.setSystemCode(regulationObj.getSystemCode());
        if (queryCriteria.getRegulationsortId() != null) {
            Regulationsort regulationsort = new Regulationsort();
            regulationsort.setId(queryCriteria.getRegulationsortId());
            operationQueryCriteria.setRegulationsort(regulationsort);
        }
        operationQueryCriteria.setCurrentVersionFlag(true);
        operationQueryCriteria.setInstStatusType(InstStatusTypeEnum.LIBRARY);
        operationQueryCriteria.setInstStatus(InstStatusEnum.ACCEPTED);
        operationQueryCriteria.setInstLevel(InstLevelEnum.OPERATING_RULE);
        return operationQueryCriteria;
    }


    public List<SystemDocSubListDto> queryLevelImage(String systemCode) {
        List<SystemDocSubListDto> multiLevelDtoList = new ArrayList<>();
        RegulationlibraryQueryCriteria queryCriteria = new RegulationlibraryQueryCriteria();
        queryCriteria.setSystemCode(systemCode);
        RegulationlibraryQueryCriteria basicQueryCriteria = this.createBasicQueryCriteria(queryCriteria);
        Sort sort = new Sort(Sort.Direction.ASC, "systemCode");
        List<SystemDocument> basicList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, basicQueryCriteria, criteriaBuilder), sort);
        basicList.forEach(basicObj -> {
            RegulationlibraryQueryCriteria regulationQueryCriteria = this.createRegulationQueryCriteria(queryCriteria, basicObj);
            List<SystemDocument> regulationList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, regulationQueryCriteria, criteriaBuilder), sort);
            SystemDocSubListDto levelOneDto = new SystemDocSubListDto();
            levelOneDto.setId(basicObj.getId());
            levelOneDto.setCode(basicObj.getSystemCode());
            levelOneDto.setName(basicObj.getSystemTitle());
            levelOneDto.setChildren(new ArrayList<>());
            multiLevelDtoList.add(levelOneDto);
            regulationList.forEach(regulationObj -> {
                SystemDocSubListDto levelTwoDto = new SystemDocSubListDto();
                levelTwoDto.setId(regulationObj.getId());
                levelTwoDto.setCode(regulationObj.getSystemCode());
                levelTwoDto.setName(regulationObj.getSystemTitle());
                levelTwoDto.setChildren(new ArrayList<>());
                levelOneDto.getChildren().add(levelTwoDto);
                RegulationlibraryQueryCriteria operationQueryCriteria = this.createOperationQueryCriteria(queryCriteria, regulationObj);
                List<SystemDocument> operationList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, operationQueryCriteria, criteriaBuilder), sort);
                operationList.forEach(operationObj -> {
                    SystemDocSubListDto levelThreeDto = new SystemDocSubListDto();
                    levelThreeDto.setId(operationObj.getId());
                    levelThreeDto.setCode(operationObj.getSystemCode());
                    levelThreeDto.setName(operationObj.getSystemTitle());
                    levelThreeDto.setChildren(new ArrayList<>());
                    levelTwoDto.getChildren().add(levelThreeDto);
                });
            });
        });
        return multiLevelDtoList;
    }

    /**
     * 查询多级制度信息
     *
     * @Author gubin
     * @Date 2020-09-16
     */
    public List<SystemDocMultiLevelDto> queryMultiLevelList(RegulationlibraryQueryCriteria queryCriteria) {
        List<SystemDocMultiLevelDto> multiLevelDtoList = new ArrayList<>();
        RegulationlibraryQueryCriteria basicQueryCriteria = this.createBasicQueryCriteria(queryCriteria);
        Sort sort = new Sort(Sort.Direction.ASC, "systemCode");
        List<SystemDocument> basicList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, basicQueryCriteria, criteriaBuilder), sort);
        basicList.forEach(basicObj -> {
            RegulationlibraryQueryCriteria regulationQueryCriteria = this.createRegulationQueryCriteria(queryCriteria, basicObj);
            List<SystemDocument> regulationList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, regulationQueryCriteria, criteriaBuilder), sort);
            if (regulationList.size() == 0) {
                SystemDocMultiLevelDto multiLevelDto = new SystemDocMultiLevelDto();
                multiLevelDto.setBasicId(basicObj.getId());
                multiLevelDto.setBasicCode(basicObj.getSystemCode());
                multiLevelDto.setBasicName(basicObj.getSystemTitle());
                multiLevelDtoList.add(multiLevelDto);
            } else {
                regulationList.forEach(regulationObj -> {
                    RegulationlibraryQueryCriteria operationQueryCriteria = this.createOperationQueryCriteria(queryCriteria, regulationObj);
                    List<SystemDocument> operationList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, operationQueryCriteria, criteriaBuilder), sort);
                    if (operationList.size() == 0) {
                        SystemDocMultiLevelDto multiLevelDto = new SystemDocMultiLevelDto();
                        multiLevelDto.setBasicId(basicObj.getId());
                        multiLevelDto.setBasicCode(basicObj.getSystemCode());
                        multiLevelDto.setBasicName(basicObj.getSystemTitle());
                        multiLevelDto.setRegulationId(regulationObj.getId());
                        multiLevelDto.setRegulationCode(regulationObj.getSystemCode());
                        multiLevelDto.setRegulationName(regulationObj.getSystemTitle());
                        multiLevelDtoList.add(multiLevelDto);
                    } else {
                        operationList.forEach(operationObj -> {
                            SystemDocMultiLevelDto multiLevelDto = new SystemDocMultiLevelDto();
                            multiLevelDto.setBasicId(basicObj.getId());
                            multiLevelDto.setBasicCode(basicObj.getSystemCode());
                            multiLevelDto.setBasicName(basicObj.getSystemTitle());
                            multiLevelDto.setRegulationId(regulationObj.getId());
                            multiLevelDto.setRegulationCode(regulationObj.getSystemCode());
                            multiLevelDto.setRegulationName(regulationObj.getSystemTitle());
                            multiLevelDto.setOperationId(operationObj.getId());
                            multiLevelDto.setOperationCode(operationObj.getSystemCode());
                            multiLevelDto.setOperationName(operationObj.getSystemTitle());
                            multiLevelDtoList.add(multiLevelDto);
                        });
                    }
                });
            }
        });
        return multiLevelDtoList;
    }

    /**
     * 查看所选制度详情
     *
     * @Author gubin
     * @Date 2020-09-16
     */
    public List<SystemDocumentDetailVo> view(String systemCode) {
        List<SystemDocumentDetailVo> voList = new ArrayList<>();
        List<SystemDocument> systemDocumentList = systemDocumentRepository.findBySystemCodeOrderByVersion(systemCode);
        systemDocumentList.forEach(systemDocument -> {
            SystemDocumentDetailVo vo = new SystemDocumentDetailVo();
            List<FileAttach> attachList = fileAttachRepository.findByMainIdAndMainTable(systemDocument.getId(), fileUploadService.getMainTable(systemDocument));
            vo.setSystemDocument(systemDocument);
            vo.setAttachList(attachList);
            voList.add(vo);
        });
        return voList;
    }


    public void abolition(RegulationlibraryQueryCriteria criteria) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        //Establishment establishment = establishmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Establishment.class,"id ",id.toString()));
        SystemDocument systemDocument = systemDocumentRepository.findById(criteria.getSystemDocumentId()).orElseGet(SystemDocument::new);
        if (!InstStatusEnum.ACCEPTED.getCode().equals(systemDocument.getInstStatus().getCode())
                && !InstStatusTypeEnum.LIBRARY.getCode().equals(systemDocument.getInstStatusType().getCode())) {
            throw new BadRequestException("该制度废除状态不符合要求！");
        }
        WorkbenchVo workbenchVo = new WorkbenchVo();
        workbenchVo.setBusinessType("abolition");
        workbenchVo.setSystemDocument(systemDocument);
        workbenchService.createProcess(workbenchVo);
        systemDocument.setInstStatus(InstStatusEnum.DOING_ABOLISH);
        //systemDocument.setInstStatusType(InstStatusTypeEnum.);
        systemDocumentRepository.save(systemDocument);
    }
}
