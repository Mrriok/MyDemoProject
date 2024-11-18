package com.zony.app.service;

import com.zony.app.domain.Regulation;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.app.repository.RegulationRepository;
import com.zony.app.repository.RegulationsortRepository;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.RegulationQueryCriteria;
import com.zony.app.service.dto.RegulationDto;
import com.zony.app.service.dto.SystemDocumentSmallDto;
import com.zony.app.service.mapstruct.RegulationMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "regulation")
public class RegulationService {
    private final RegulationRepository regulationRepository;
    private final RegulationsortRepository regulationsortRepository;
    private final SystemDocumentRepository systemDocumentRepository;
    private final RegulationMapper regulationMapper;
    private final WorkbenchService workbenchService;
    private final FileUploadService fileUploadService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(RegulationService.class);

    @Transactional(rollbackFor = Exception.class)
    public void create(RegulationDto regulationDto) {
        Regulation regulation = dtoToEntity(regulationDto);
        regulation.setStatus(ProgressStatusEnum.NO_STARTED);
        regulation.setDeptId(userRepository.findDeptIdByUsername(regulation.getInitUser().getUsername()));
        if (!StringUtils.isEmpty(regulation.getDelayReason())){
            regulation.setIsSubmit(true);
        }else {
            regulation.setIsSubmit(false);
        }
        regulationRepository.save(regulation);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(RegulationDto regulationDto) {
        Regulation regulation = regulationRepository.findById(regulationDto.getId()).orElseGet(Regulation::new);
        if(!SecurityUtils.getCurrentUsername().equals(regulation.getCreateBy())){ //是否进行权限校验,权限校验最好在哪一层
            throw new BadRequestException("权限不足！您无法更新制度名称为："+regulation.getInstName()+" 的记录。");
        }
        regulation = dtoToEntity(regulationDto);
        if(ProgressStatusEnum.DELAY_INCOMPLETE.getCode().equals(regulation.getStatus().getCode())
         && StringUtils.isEmpty(regulation.getDelayReason())){
            throw new BadRequestException("延期计划必须提交延期原因后，才能进行更改！");
        }
        if (!StringUtils.isEmpty(regulation.getDelayReason())){
            regulation.setIsSubmit(true);
        }else {
            regulation.setIsSubmit(false);
        }
        //if (ObjectUtils.isEmpty(regulation)) {
        //    throw new EntityExistException(Regulation.class, "id", regulationDto.getId() + "");
        //}
        regulationRepository.save(regulation);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        //ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        //权限校验
        //Set<Long> ids = new HashSet<>();
        List<Regulation> list =  regulationRepository.findAllById(ids);
        for(Regulation item:list){
            if(!SecurityUtils.getCurrentUsername().equals(item.getCreateBy())){ //是否进行权限校验,权限校验最好在哪一层
                throw new BadRequestException("权限不足！您无法删除制度名称为："+item.getInstName()+" 的记录。");
            }
            //fileUploadService.delete(os,item);
            ids.add(item.getId());
        }
        try {
            regulationRepository.deleteAllByIdIn(ids);
        }catch (Exception e){
            logger.debug(e.getMessage());
            throw new BadRequestException("删除数据中存在被其他数据引用的记录，请清除子记录后删除！");
        }
    }

    public Object queryAll(RegulationQueryCriteria queryCriteria, Pageable pageable) {
        Page<Regulation> page = regulationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder), pageable);
        //List<Regulation> regulationList = page.getContent();
        return PageUtil.toPage(page.map(regulationMapper::toDto));
    }

    public Object queryAllInfo(RegulationQueryCriteria queryCriteria, Pageable pageable) {
        Map<String,Object> resultMap = new HashMap<>();
        List<ProgressStatusEnum> statusList = new ArrayList<>();
        Integer pageSize = pageable.getPageSize();
        Integer pageNumber = pageable.getPageNumber();
        Integer startIndex = pageNumber*pageSize;
        Integer endIndex = pageNumber*pageSize + pageSize;

        statusList.add(ProgressStatusEnum.END_OF_APPROVAL);
        statusList.add(ProgressStatusEnum.DELAY_INCOMPLETE);
        List<Regulation> regulationList = regulationRepository.findAllByDeptIdAndStatusIn(queryCriteria.getDeptId(),statusList);
        //Page<Regulation> page = regulationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder), pageable);
        //List<Regulation> regulationList = page.getContent();
        List<Regulation> regulationSubList = new ArrayList<>();
        List<RegulationDto> regulationDtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(regulationList)){
            Integer total = regulationList.size();
            if( total > startIndex ){
                endIndex = total < endIndex ? total : endIndex;
                regulationSubList = regulationList.subList(startIndex,endIndex);
                regulationSubList.forEach(item->{
                    regulationDtoList.add(entityToDto(item));
                });
            }else if(total < startIndex){
                resultMap.put("content",new ArrayList<>());
            }
        }
        resultMap.put("content",regulationDtoList);
        resultMap.put("totalElements",regulationList.size());
        return resultMap;
    }

    public RegulationDto findById(Long id) {
        Regulation regulation = regulationRepository.findById(id).orElseGet(Regulation::new);
        //regulation.setRegulationName();
        RegulationDto regulationDto = entityToDto(regulation);
        return regulationDto;
    }

    public void operating(RegulationQueryCriteria criteria) {
        //User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow(() -> new EntityNotFoundException(User.class,"CurrentUserId ",SecurityUtils.getCurrentUserId().toString()));
        //Establishment establishment = establishmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Establishment.class,"id ",id.toString()));
        Regulation regulation = regulationRepository.findById(Long.valueOf(criteria.getId())).orElseGet(Regulation::new);
        if(!ProgressStatusEnum.NO_STARTED.getCode().equals(regulation.getStatus().getCode())){
            throw new BadRequestException("该制度状态不符合要求！");
        }
        if(!SecurityUtils.getCurrentUsername().equals(regulation.getCreateBy())){ //是否进行权限校验,权限校验最好在哪一层
            throw new BadRequestException("权限不足！您无法发起制度名称为："+regulation.getInstName()+" 的记录。");
        }
        WorkbenchVo workbenchVo = new WorkbenchVo();
        workbenchVo.setBusinessType("plan");
        workbenchVo.setRegulation(regulation);
        workbenchService.createProcess(workbenchVo);
        regulation.setStatus(ProgressStatusEnum.PROGRESS);
        regulationRepository.save(regulation);
    }
    public Regulation dtoToEntity(RegulationDto regulationDto){
        //转换基本信息
        Regulation regulation = regulationMapper.toEntity(regulationDto);
        //转换编制依据，主要应对风险与目标，释义，适用范围
        JsonUtil jsonUtil = JsonUtil.getInstance();
        //List<Long> referenceIds = new ArrayList<>();
        //if(!CollectionUtils.isEmpty(regulationDto.getReferenceList())){
        //    regulationDto.getReferenceList().forEach(item->{
        //        referenceIds.add(item.getId());
        //    });
        //    regulation.setReference(jsonUtil.obj2json(referenceIds));
        //}else {
        //    regulation.setReference(new String());
        //}

        //List<Long> docAccordingIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(regulationDto.getDocAccordingList())){
            regulation.setDocAccording(jsonUtil.obj2json(regulationDto.getDocAccordingList()));
        }else {
            regulation.setDocAccording(null);
        }
        List<Long> abolishIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(regulationDto.getAbolishList())){
            regulationDto.getAbolishList().forEach(item->{
                abolishIds.add(item.getId());
            });
            regulation.setAbolish(jsonUtil.obj2json(abolishIds));
        }else {
            regulation.setAbolish(null);
        }
        return regulation;
    }
    public RegulationDto entityToDto(Regulation regulation){
        RegulationDto regulationDto = regulationMapper.toDto(regulation);

        //转换联系人
        JsonUtil jsonUtil = JsonUtil.getInstance();
        if (!StringUtils.isEmpty(regulation.getDocAccording())){
            regulationDto.setDocAccordingList((List<Map<String, Object>>) jsonUtil.json2obj(regulation.getDocAccording(),List.class));
        }else {
            regulationDto.setDocAccordingList(new ArrayList<>());
        }

        if (!StringUtils.isEmpty(regulation.getAbolish())){
            List<Integer> abolishIntegerIds = (List<Integer>) jsonUtil.json2obj(regulation.getAbolish(),List.class);
            List<Long> abolishLongIds = abolishIntegerIds.stream().map(i->Long.valueOf(i.toString())).collect(Collectors.toList());
            regulationDto.setAbolishList(systemDocumentRepository.findAllById(abolishLongIds));
        }else {
            regulationDto.setAbolishList(new ArrayList<>());
        }

        return regulationDto;
    }

    public List getExcelList(Set<Long> ids) {
        //存放结果集
        List<Object> resultList = new ArrayList<>();
        //存放实体
        List<Regulation> regulationList = regulationRepository.findAllById(ids);
        if(!CollectionUtils.isEmpty(regulationList)){
            Integer i = 1;
            JsonUtil jsonUtil = JsonUtil.getInstance();
            for(Regulation item:regulationList){
                Map<String,Object> map = new HashMap<>();
                map.put("no",i);
                map.put("systemTitle",item.getInstName());
                map.put("systemCode",item.getInstCode());
                map.put("instLevelName",item.getInstLevel().getName());
                map.put("instPropertyName",item.getInstProperty().getName());
                map.put("initDeptName",item.getInitUser().getDept().getName());
                map.put("expectedTime",item.getExpectedTime());
                map.put("status",item.getStatus().getName());
                map.put("reason",item.getReason());
                map.put("delayReason",item.getDelayReason());
                if (!StringUtils.isEmpty(item.getDocAccording())){
                    List<Map<String,Object>> docAccordingDtoList = (List<Map<String, Object>>) jsonUtil.json2obj(item.getDocAccording(),List.class);
                        StringBuilder docAccordingStr = new StringBuilder();
                        for(Map<String,Object> it:docAccordingDtoList){
                            if (!StringUtils.isEmpty(MapUtils.getString(it,"systemCode"))) {
                                docAccordingStr.append(MapUtils.getString(it,"systemCode")).append("-");
                            }
                            docAccordingStr.append(MapUtils.getString(it,"systemTitle")).append("  ");
                        }
                        //for(SystemDocumentSmallDto it:docAccordingDtoList){
                        //    if (!StringUtils.isEmpty(it.getSystemCode())) {
                        //        docAccordingStr.append(it.getSystemCode()).append("-");
                        //    }
                        //    docAccordingStr.append(it.getSystemTitle()).append("  ");
                        //}
                        map.put("docAccording",docAccordingStr.toString());
                }else {
                    map.put("docAccording","N/A");
                }

                if (!StringUtils.isEmpty(item.getAbolish())){
                    List<Integer> abolishIntegerIds = (List<Integer>) jsonUtil.json2obj(item.getAbolish(),List.class);
                    List<Long> abolishLongIds = abolishIntegerIds.stream().map(x->Long.valueOf(x.toString())).collect(Collectors.toList());
                    List<SystemDocument> abolishList = systemDocumentRepository.findAllById(abolishLongIds);
                    StringBuilder abolishStr = new StringBuilder();
                    for(SystemDocument it:abolishList){
                        abolishStr.append(it.getSystemCode()).append("-")
                                .append(it.getSystemTitle()).append("   ");
                    }
                    map.put("abolish",abolishStr.toString());
                }else {
                    map.put("abolish","N/A");
                }
                resultList.add(map);
                i++;
            }
        }
        return resultList;
    }
}
