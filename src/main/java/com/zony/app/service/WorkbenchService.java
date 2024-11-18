/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.zony.app.domain.*;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.*;
import com.zony.app.repository.*;
import com.zony.app.service.criteria.WorkbenchLogQueryCriteria;
import com.zony.app.service.criteria.WorkbenchQueryCriteria;
import com.zony.app.service.dto.*;
import com.zony.app.service.mapstruct.DefineToDefineEveryMapper;
import com.zony.app.service.mapstruct.OpinionMapper;
import com.zony.app.service.mapstruct.RegulationMapper;
import com.zony.app.utils.ReflectUtil;
import com.zony.common.domain.vo.EmailVo;
import com.zony.common.exception.BadRequestException;
import com.zony.common.service.EmailService;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.SecurityUtils;
import com.zony.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/11 -16:19
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "workbench")
public class WorkbenchService {


    private final UserRepository userRepository;
    private final WorkflowCommonRepository commonRepository;
    //private final WorkbenchRepository workbenchRepository;
    private final WorkflowDefineRepository defineRepository;
    private final WorkflowDefineEveryOneRepository everyOneRepository;
    private final WorkflowLogRepository logRepository;
    private final WorkflowOpinionRepository workflowOpinionRepository;
    private final WorkflowEstablishmentRepository workflowEstablishmentRepository;
    private final DefineToDefineEveryMapper defineToDefineEveryMapper;
    private final FeedbackOpinionRepository feedbackOpinionRepository;
    private final WorkflowSystemDocumentRepository workflowSystemDocumentRepository;
    private final WorkflowRegulationPlanRepository workflowRegulationPlanRepository;
    private final EstablishmentRepository establishmentRepository;
    private final OpinionRepository opinionRepository;
    private final SystemDocumentRepository systemDocumentRepository;
    private final RegulationRepository regulationRepository;
    private final EmailService emailService;
    private final FileUploadService fileUploadService;
    private final RegulationMapper regulationMapper;
    private final OpinionMapper opinionMapper;
    private final DocReplyRiskTargetRepository docReplyRiskTargetRepository;
    private final DocExplainRepository docExplainRepository;
    private final BaselibraryRepository baselibraryRepository;
    //private final RegulationService regulationService;
    @PersistenceContext
    private EntityManager em;

    //新建一个流程
    @Transactional(rollbackFor = Exception.class)
    public void createProcess(WorkbenchVo workbenchVo){

        String businessType = workbenchVo.getBusinessType();
        WorkflowCommon workflowCommon = new WorkflowCommon();
        if (StringUtils.isEmpty(businessType)){
            throw new BadRequestException("必填字段不能为空！");
        }
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        Integer stepLength = 1;
        switch (businessType) {
            case "establishment": {
                WorkflowSystemDocument workflowSystemDocument = new WorkflowSystemDocument();
                SystemDocument systemDocument = workbenchVo.getSystemDocument();
                if(ObjectUtils.isEmpty(systemDocument)){
                    throw new BadRequestException("必填字段不能为空！");
                }
                workflowSystemDocument.setSystemDocument(systemDocument);
                //查询流程基本定义
                WorkflowDefine define1 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.SYSTEM,1);
                WorkflowDefine define2 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.SYSTEM,2);
                define1.setDisposeObjType(WorkflowDisposeObjEnum.USER);
                define1.setDisposeObjId("," + user.getUsername() + ",");
                //将每步流程定义存放到everyOne
                WorkflowDefineEveryOne defineEveryOne1 = defineToDefineEveryMapper.toDto(define1);
                WorkflowDefineEveryOne defineEveryOne2 = defineToDefineEveryMapper.toDto(define2);
                defineEveryOne1.setStepLength(stepLength);
                defineEveryOne2.setStepLength(stepLength + 1);
                String disposeObjId = defineEveryOne2.getDisposeObjId();
                disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
                List<String> objIds = Arrays.asList(disposeObjId.split(","));
                //设置流程基本信息
                workflowCommon.setWorkflowNum(systemDocument.getSystemCode());
                workflowCommon.setWorkflowTitle(systemDocument.getSystemTitle());
                workflowCommon.setWorkflowType(WorkflowClassEnum.SYSTEM);
                workflowCommon.setStartUsername(user.getUsername());
                workflowCommon.setStepLength(stepLength);
                workflowCommon.setNowStepNum(1);
                workflowCommon.setFinishFlag(false);
                workflowCommon.setPreFlag(true);
                workflowCommon.setHandlerNum(1);
                WorkflowCommon workflowCommon1 = commonRepository.save(workflowCommon);
                defineEveryOne1.setWorkflowCommonId(workflowCommon1.getId());
                defineEveryOne2.setWorkflowCommonId(workflowCommon1.getId());
                everyOneRepository.save(defineEveryOne1);
                everyOneRepository.save(defineEveryOne2);
                //设置发起记录
                saveLog(workflowCommon1,defineEveryOne1,user.getUsername(),null,null,null);

                workflowCommon1.setHandlerNum(objIds.size());
                workflowCommon1.setNowStepNum(2);
                workflowCommon1.setFinishFlag(false);
                workflowCommon1.setStepLength(stepLength + 1);
                commonRepository.save(workflowCommon1);
                workflowSystemDocument.setWorkflowCommon(workflowCommon1);
                workflowSystemDocumentRepository.save(workflowSystemDocument);
                remindUsers(objIds,WorkflowDisposeObjEnum.USER,workflowCommon1);
                //establishmentRepository.save(establishment);
                break;
            }
            case "opinion": {
                WorkflowOpinion workflowOpinion = new WorkflowOpinion();
                Opinion opinion = workbenchVo.getOpinion();
                if(ObjectUtils.isEmpty(opinion)){
                    throw new BadRequestException("必填字段不能为空！");
                }
                workflowOpinion.setOpinion(opinion);
                //查询流程基本定义
                WorkflowDefine define1 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.OPINION,1);
                WorkflowDefine define2 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.OPINION,2);
                define1.setDisposeObjType(WorkflowDisposeObjEnum.USER);
                define1.setDisposeObjId("," + user.getUsername() + ",");
                //将每步流程定义存放到everyOne
                WorkflowDefineEveryOne defineEveryOne1 = defineToDefineEveryMapper.toDto(define1);
                WorkflowDefineEveryOne defineEveryOne2 = defineToDefineEveryMapper.toDto(define2);
                defineEveryOne1.setStepLength(stepLength);
                defineEveryOne2.setStepLength(stepLength + 1);
                //增求意见节点需要从实体中获取联系人
                JsonUtil jsonUtil = JsonUtil.getInstance();
                String disposeObjId = opinion.getContactPersonIds();
                List<String> objIds = (List<String>) jsonUtil.json2obj(disposeObjId,List.class);
                defineEveryOne2.setDisposeObjId(","+StringUtils.join(objIds,',')+",");
                //设置流程基本信息
                workflowCommon.setWorkflowNum(opinion.getInstCode().toString());
                workflowCommon.setWorkflowTitle(opinion.getInstName());
                workflowCommon.setWorkflowType(WorkflowClassEnum.OPINION);
                workflowCommon.setStartUsername(user.getUsername());
                workflowCommon.setStepLength(stepLength);
                workflowCommon.setNowStepNum(1);
                workflowCommon.setFinishFlag(false);
                workflowCommon.setPreFlag(true);
                workflowCommon.setHandlerNum(1);
                WorkflowCommon workflowCommon1 = commonRepository.save(workflowCommon);
                defineEveryOne1.setWorkflowCommonId(workflowCommon1.getId());
                defineEveryOne2.setWorkflowCommonId(workflowCommon1.getId());
                everyOneRepository.save(defineEveryOne1);
                everyOneRepository.save(defineEveryOne2);
                //设置发起记录
                saveLog(workflowCommon1,defineEveryOne1,user.getUsername(),null,null,null);

                workflowCommon1.setHandlerNum(objIds.size());
                workflowCommon1.setNowStepNum(2);
                workflowCommon1.setFinishFlag(false);
                workflowCommon1.setStepLength(stepLength + 1);
                commonRepository.save(workflowCommon1);
                workflowOpinion.setWorkflowCommon(workflowCommon1);
                workflowOpinionRepository.save(workflowOpinion);
                remindUsers(objIds,WorkflowDisposeObjEnum.USER,workflowCommon1);
                //establishmentRepository.save(establishment);
                break;
            }
            case "plan": {
                WorkflowRegulationPlan workflowRegulationPlan = new WorkflowRegulationPlan();
                Regulation regulation = workbenchVo.getRegulation();
                if(ObjectUtils.isEmpty(regulation)){
                    throw new BadRequestException("必填字段不能为空！");
                }
                workflowRegulationPlan.setRegulation(regulation);
                //查询流程基本定义
                WorkflowDefine define1 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.PLAN,1);
                WorkflowDefine define2 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.PLAN,2);
                define1.setDisposeObjType(WorkflowDisposeObjEnum.USER);
                define1.setDisposeObjId("," + user.getUsername() + ",");
                //将每步流程定义存放到everyOne
                WorkflowDefineEveryOne defineEveryOne1 = defineToDefineEveryMapper.toDto(define1);
                WorkflowDefineEveryOne defineEveryOne2 = defineToDefineEveryMapper.toDto(define2);
                defineEveryOne1.setStepLength(stepLength);
                defineEveryOne2.setStepLength(stepLength + 1);
                //以上流程第二步均为风控办审批，计划第二步为发起人处长，动态的需要动态的查询
                defineEveryOne2.setDisposeObjType(WorkflowDisposeObjEnum.DEPT);
                defineEveryOne2.setDisposeObjId("," + user.getDept().getId().toString() + ",");

                String disposeObjId = defineEveryOne2.getDisposeObjId();
                //String disposeObjId = user.getDept();
                 disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
                List<String> objIds = Arrays.asList(disposeObjId.split(","));
                //设置流程基本信息
                workflowCommon.setWorkflowNum(regulation.getInstCode());
                workflowCommon.setWorkflowTitle(regulation.getInstName());
                workflowCommon.setWorkflowType(WorkflowClassEnum.PLAN);
                workflowCommon.setStartUsername(user.getUsername());
                workflowCommon.setStepLength(stepLength);
                workflowCommon.setNowStepNum(1);
                workflowCommon.setFinishFlag(false);
                workflowCommon.setPreFlag(true);
                workflowCommon.setHandlerNum(1);
                WorkflowCommon workflowCommon1 = commonRepository.save(workflowCommon);
                defineEveryOne1.setWorkflowCommonId(workflowCommon1.getId());
                defineEveryOne2.setWorkflowCommonId(workflowCommon1.getId());
                everyOneRepository.save(defineEveryOne1);
                everyOneRepository.save(defineEveryOne2);
                //设置发起记录
                saveLog(workflowCommon1,defineEveryOne1,user.getUsername(),null,null,null);

                workflowCommon1.setHandlerNum(objIds.size());
                workflowCommon1.setNowStepNum(2);
                workflowCommon1.setFinishFlag(false);
                workflowCommon1.setStepLength(stepLength + 1);
                commonRepository.save(workflowCommon1);
                workflowRegulationPlan.setWorkflowCommon(workflowCommon1);
                workflowRegulationPlanRepository.save(workflowRegulationPlan);
                remindUsers(objIds,defineEveryOne2.getDisposeObjType(),workflowCommon1);
                break;
            }
            case "abolition":{
                WorkflowSystemDocument workflowSystemDocument = new WorkflowSystemDocument();
                SystemDocument systemDocument = workbenchVo.getSystemDocument();
                if(ObjectUtils.isEmpty(systemDocument)){
                    throw new BadRequestException("必填字段不能为空！");
                }
                workflowSystemDocument.setSystemDocument(systemDocument);
                //查询流程基本定义
                WorkflowDefine define1 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.ABOLITION,1);
                WorkflowDefine define2 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.ABOLITION,2);
                define1.setDisposeObjType(WorkflowDisposeObjEnum.USER);
                define1.setDisposeObjId("," + user.getUsername() + ",");
                //将每步流程定义存放到everyOne
                WorkflowDefineEveryOne defineEveryOne1 = defineToDefineEveryMapper.toDto(define1);
                WorkflowDefineEveryOne defineEveryOne2 = defineToDefineEveryMapper.toDto(define2);
                defineEveryOne1.setStepLength(stepLength);
                defineEveryOne2.setStepLength(stepLength + 1);
                String disposeObjId = defineEveryOne2.getDisposeObjId();
                disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
                List<String> objIds = Arrays.asList(disposeObjId.split(","));
                //设置流程基本信息
                workflowCommon.setWorkflowNum(systemDocument.getSystemCode());
                workflowCommon.setWorkflowTitle(systemDocument.getSystemTitle());
                workflowCommon.setWorkflowType(WorkflowClassEnum.ABOLITION);
                workflowCommon.setStartUsername(user.getUsername());
                workflowCommon.setStepLength(stepLength);
                workflowCommon.setNowStepNum(1);
                workflowCommon.setFinishFlag(false);
                workflowCommon.setPreFlag(true);
                workflowCommon.setHandlerNum(1);
                WorkflowCommon workflowCommon1 = commonRepository.save(workflowCommon);
                defineEveryOne1.setWorkflowCommonId(workflowCommon1.getId());
                defineEveryOne2.setWorkflowCommonId(workflowCommon1.getId());
                everyOneRepository.save(defineEveryOne1);
                everyOneRepository.save(defineEveryOne2);
                //设置发起记录
                saveLog(workflowCommon1,defineEveryOne1,user.getUsername(),null,null,null);

                workflowCommon1.setHandlerNum(objIds.size());
                workflowCommon1.setNowStepNum(2);
                workflowCommon1.setFinishFlag(false);
                workflowCommon1.setStepLength(stepLength + 1);
                commonRepository.save(workflowCommon1);
                workflowSystemDocument.setWorkflowCommon(workflowCommon1);
                workflowSystemDocumentRepository.save(workflowSystemDocument);
                remindUsers(objIds,WorkflowDisposeObjEnum.USER,workflowCommon1);
                //establishmentRepository.save(establishment);
                break;
            }
        }
    }
    public List<WorkbenchTodoDto> queryTodoList(WorkbenchQueryCriteria queryCriteria) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //构建sql 主体
        //order by common.id desc
        StringBuilder sql = new StringBuilder()
        .append("select new com.zony.app.service.dto.WorkbenchTodoDto(common.id,common.workflowType,common.workflowNum,common.workflowTitle,common.nowStepNum,common.startUsername,common.finishFlag,common.createTime,defineEveryOne.stepName,defineEveryOne.stepSeqNum,defineEveryOne.disposeObjId,defineEveryOne.disposeObjType,defineEveryOne.disposeWayType,user.nickName)" +
                " from WorkflowCommon common,WorkflowDefineEveryOne defineEveryOne,User user where common.id = defineEveryOne.workflowCommonId and common.nowStepNum = defineEveryOne.stepSeqNum and common.stepLength = defineEveryOne.stepLength and common.finishFlag = 0 and user.username = common.startUsername");
        Map<String,Object> paramMap = new HashMap<>();
        //构建sql查询  循环版 还需判断那个表内，以及等于还是like,
        Field[] fields = queryCriteria.getClass().getDeclaredFields();
        for(int i = 0;i<fields.length;i++){
            String name = fields[i].getName();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method m = queryCriteria.getClass().getMethod(methodName);
            Object value = m.invoke(queryCriteria);
            if(value != null){
                //define
                if(name.equals("disposeObjId") || name.equals("stepName")){
                    sql.append(" and defineEveryOne."+name + " like :" +name);
                }else if(name.equals("disposeObjType") || name.equals("disposeWayType") ||  name.equals("stepSeqNum")){
                    sql.append(" and defineEveryOne."+name + " = :" +name);
                }
                //common
                else if(name.equals("workflowNum") || name.equals("workflowTitle") ) {
                    sql.append(" and common." + name + " like :" +name);
                } else if(name.equals("id") || name.equals("workflowType") ||  name.equals("nowStepNum") || name.equals("startUsername")) {
                    sql.append(" and common." + name + " = :" +name);
                }else if(name.equals("initTime")) {
                    sql.append(" and common." + "initTime" + " between :startTime and :endTime");
                }else {//startUsernameList
                    sql.append(" and common."+name + " in :(" +name+")");
                }
                paramMap.put(name,value);
            }
        }
        ////添加排序
        //String sort = pageable.getSort().toString();
        //if (!"UNSORTED".equals(sort)){
        //    String newSort = sort.replace(":"," ").replace(", "," common.");
        //    sql.append(" order by " + "common." + newSort);
        //}
        //添加参数
        Query query = em.createQuery(sql.toString());
        for (Map.Entry<String,Object> entry: paramMap.entrySet()){
            String key = entry.getKey();
            if ("disposeObjId".equals(key)){
                StringBuilder value = new StringBuilder("%,").append(entry.getValue().toString()).append(",%");
                query.setParameter(key,value.toString());
            }else if("stepName".equals(key)||"workflowNum".equals(key)||"workflowTitle".equals(key)){
                StringBuilder value = new StringBuilder("%").append(entry.getValue().toString()).append("%");
                query.setParameter(key,value.toString());
            } else if("initTime".equals(key)){
                List<Timestamp> initTimeList = (List<Timestamp>) entry.getValue();
                query.setParameter("startTime",initTimeList.get(0));
                query.setParameter("endTime",initTimeList.get(1));
            }else {
                query.setParameter(entry.getKey(),entry.getValue());
            }
        }
        //query.setFirstResult(startIndex).setMaxResults(endIndex);
        return query.getResultList();
    }

    public List<WorkbenchDoneDto> queryDoneList(WorkbenchLogQueryCriteria queryCriteria) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {


        //构建sql 主体
        //order by common.id desc
        StringBuilder sql = new StringBuilder()
                .append("select new com.zony.app.service.dto.WorkbenchDoneDto(common.id ,common.workflowType,common.workflowNum,common.workflowTitle,common.nowStepNum,common.startUsername,common.finishFlag,log.createTime,log.stepName,log.stepSeqNum,log.username,log.agreeFlag,user.username,log.opinion,log.stepLength)" +
                        " from WorkflowCommon common,WorkflowLog log,User user where common.id = log.workflowCommon and user.username = common.startUsername");

        Map<String,Object> paramMap = new HashMap<>();
        //构建sql查询  循环版 还需判断那个表内，以及等于还是like,
        Field[] fields = queryCriteria.getClass().getDeclaredFields();
        for(int i = 0;i<fields.length;i++){
            String name = fields[i].getName();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method m = queryCriteria.getClass().getMethod(methodName);
            Object value = m.invoke(queryCriteria);
            if(value != null){
                //log
                if(name.equals("id") || name.equals("stepSeqNum") || name.equals("username") || name.equals("agreeFlag") || name.equals("stepLength")){
                    sql.append(" and log." + name + " = :" +name);
                }else if(name.equals("stepName")){
                    sql.append(" and log." + name + " like :" +name);
                }
                //common
                else if(name.equals("workflowNum") || name.equals("workflowTitle") ) {
                    sql.append(" and common." + name + " like :" +name);
                }else if(name.equals("initTime")) {
                    sql.append(" and common." + "initTime" + " between :startTime and :endTime");
                } else if(name.equals("workflowType") || name.equals("startUsername")) {
                    sql.append(" and common." + name + " = :" +name);
                }else {//startUsernameList
                    sql.append(" and common."+name + " in :(" +name+")");
                }
                paramMap.put(name,value);
            }
        }
        //添加排序
        //String sort = pageable.getSort().toString();
        //if (!"UNSORTED".equals(sort)){
        //    String newSort = sort.replace(":"," ").replace(", "," common.");
        //    sql.append(" order by " + "common." + newSort);
        //}
        //添加参数
        Query query = em.createQuery(sql.toString());
        for (Map.Entry<String,Object> entry: paramMap.entrySet()){
            String key = entry.getKey();
            if("stepName".equals(key)||"workflowNum".equals(key)||"workflowTitle".equals(key)){
                StringBuilder value = new StringBuilder("%").append(entry.getValue().toString()).append("%");
                query.setParameter(key,value.toString());
            } else if("initTime".equals(key)){
                List<Timestamp> initTimeList = (List<Timestamp>) entry.getValue();
                query.setParameter("startTime",initTimeList.get(0));
                query.setParameter("endTime",initTimeList.get(1));
            } else {
                query.setParameter(entry.getKey(),entry.getValue());
            }
        }
        //query.setFirstResult(startIndex).setMaxResults(endIndex);
        return query.getResultList();
    }


    private void saveLog(WorkflowCommon workflowCommon,WorkflowDefineEveryOne defineEveryOne,String username,Boolean agreeFlag,String reminder,String opinion){
        WorkflowLog workflowLog = new WorkflowLog();
        //workflowCommon.setNowStepNum(nodeEveryOne.getId());
        workflowLog.setStepSeqNum(defineEveryOne.getStepSeqNum());
        workflowLog.setStepName(defineEveryOne.getStepName());
        workflowLog.setUsername(username);
        workflowLog.setAgreeFlag(agreeFlag);
        workflowLog.setReminder(reminder);
        workflowLog.setOpinion(opinion);
        workflowLog.setWorkflowCommon(workflowCommon);
        workflowLog.setStepLength(workflowCommon.getStepLength());
        logRepository.save(workflowLog);
    }
    //private void remindUsers(String disposeObjId,WorkflowDisposeObjEnum workflowDisposeObjEnum) {
    //    //String disposeObjId = workflowDefine.getDisposeObjId();
    //    disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
    //    List<String> objIds = Arrays.asList(disposeObjId.split(","));
    //    //WorkflowDisposeObjEnum workflowDisposeObjEnum = workflowDefine.getDisposeObjType();
    //    switch (workflowDisposeObjEnum.getCode()){
    //        case "user":{
    //
    //            break;
    //        }
    //        case "dept":{
    //
    //            break;
    //        }
    //        case "role":{
    //
    //            break;
    //        }
    //    }
    //}

    //查询待办详情
    @Transactional(rollbackFor = Exception.class)
    public Object todoView(WorkbenchQueryCriteria workbenchQueryCriteria){
        Map<String,Object> resultMap = new HashMap<>();
        Long commonId = workbenchQueryCriteria.getId();
        WorkflowClassEnum workflowType = workbenchQueryCriteria.getWorkflowType();
        //Integer stepLength = workbenchQueryCriteria.getStepLength();
        WorkflowCommon workflowCommon = commonRepository.findById(commonId).orElseGet(WorkflowCommon::new);
        //workflowCommon.setId(commonId);
        //查找审批记录
        List<WorkflowLog> workflowLogList = logRepository.findByWorkflowCommon(workflowCommon);
        //WorkflowDefineEveryOne defineEveryOne = everyOneRepository.findByWorkflowCommonIdAndAndStepLength(commonId,workbenchQueryCriteria.getStepLength());

        Map<String,Object> definePropertiesMap = new HashMap<>();
        WorkflowDefineEveryOne defineEveryOne = everyOneRepository.findByWorkflowCommonIdAndAndStepLength(workflowCommon.getId(),workflowCommon.getStepLength());
        definePropertiesMap.put("stepName",defineEveryOne.getStepName());
        definePropertiesMap.put("stepSeqNum",defineEveryOne.getStepSeqNum());
        //对参数处理  system_approval system_approval
        List<Object> workflowLogMapList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(workflowLogList)){
            //对workflowLogList排序
            List<WorkflowLog> workflowLogSortList = workflowLogList.stream().sorted(Comparator.comparing(WorkflowLog::getCreateTime)).collect(Collectors.toList());
            //处理审批记录
            workflowLogSortList.forEach(item->{
                //BeanMap beanMap = new BeanMap(item);
                Map<String,Object> propertiesMap = new HashMap<>();
                //查询处理者部门
                String organizerName = new String();
                if(item.getStepSeqNum() == 1){
                    User user = userRepository.findByUsername(item.getUsername());
                    organizerName = user.getNickName();
                    propertiesMap.put("accepterName","");
                }else {
                    User accepter = userRepository.findByUsername(item.getUsername());
                    propertiesMap.put("accepterName",accepter.getUsername());
                }
                propertiesMap.put("organizerName",organizerName);

                //翻译agreeFlag
                if(item.getAgreeFlag() != null &&item.getAgreeFlag()){
                    propertiesMap.put("agreeFlagLabel","采纳");
                }else if(item.getAgreeFlag() != null){
                    propertiesMap.put("agreeFlagLabel","不采纳");
                }
                workflowLogMapList.add(ReflectUtil.getObject(item,propertiesMap));
            });
        }
        //.stream().sorted().collect(Collectors.toList());
        resultMap.put("logList",workflowLogMapList);

        switch (workflowType.getCode()){
            case "solicit_opinions":{
                definePropertiesMap.put("workflowCommon",workflowCommon);
                //查找实体


                //查找实体
                WorkflowOpinion workflowOpinion = workflowOpinionRepository.findByWorkflowCommon(workflowCommon);
                Opinion opinion = workflowOpinion.getOpinion();
                OpinionDto opinionDto = opinionMapper.toDto(opinion);

                //转换联系人
                JsonUtil jsonUtil = JsonUtil.getInstance();
                if (!StringUtils.isEmpty(opinion.getContactPersonIds())){
                    List<String> usernames = (List<String>) jsonUtil.json2obj(opinion.getContactPersonIds(),List.class);
                    opinionDto.setContactPersonList(userRepository.findByUsernameIn(usernames));
                }else {
                    opinionDto.setContactPersonList(new ArrayList<>());
                }

                definePropertiesMap.put("opinion",opinionDto);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 2){
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,2);
                }else {
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                }
                //feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                //查找附件
                List<FileAttachDto> fileAttachDtoList = fileUploadService.view(workflowOpinion.getOpinion());
                resultMap.put("attachList",fileAttachDtoList);
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",definePropertiesMap);
                resultMap.put("businessType","opinion");
                break;
            }
            case "system_approval":{

                //查找实体
                WorkflowSystemDocument workflowSystemDocument = workflowSystemDocumentRepository.findByWorkflowCommon(workflowCommon);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 4){
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,4);
                }else {
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                }
                //查找附件
                List<FileAttachDto> fileAttachDtoList = fileUploadService.view(workflowSystemDocument.getSystemDocument());
                resultMap.put("attachList",fileAttachDtoList);
                //Establishment establishment = workflowEstablishment.getEstablishment();
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",ReflectUtil.getObject(workflowSystemDocument,definePropertiesMap));
                resultMap.put("businessType","establishment");
                break;
            }
            case "system_abolition":{
                //查找实体
                WorkflowSystemDocument workflowSystemDocument = workflowSystemDocumentRepository.findByWorkflowCommon(workflowCommon);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 4){
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,4);
                }else {
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                }
                //Map<String,Object> prop = new HashMap<>();
                SystemDocument systemDocument = workflowSystemDocument.getSystemDocument();
                List<FileAttachDto> fileAttachDtoList = fileUploadService.view(systemDocument);
                //查找附件
                resultMap.put("attachList",fileAttachDtoList);
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",ReflectUtil.getObject(workflowSystemDocument,definePropertiesMap));
                resultMap.put("businessType","abolition");
                break;
            }
            case "system_plan":{
                //Map<String,Object> attributeMap = new HashMap<>();
                //Map<String,Object> workflowRegulationPlanMap = new HashMap<>();
                definePropertiesMap.put("workflowCommon",workflowCommon);
                //查找实体
                WorkflowRegulationPlan workflowRegulationPlan = workflowRegulationPlanRepository.findByWorkflowCommon(workflowCommon);
                Regulation regulation = workflowRegulationPlan.getRegulation();
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
                definePropertiesMap.put("regulation",regulationDto);
                //workflowRegulationPlanMap.put("");
                //attributeMap.put("regulationDto",regulationDto);
                //Object entity = ReflectUtil.getObject(workflowRegulationPlan,attributeMap);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 4){
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,4);
                }else {
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                }
                //feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",definePropertiesMap);
                resultMap.put("businessType","plan");
            }
        }
        return resultMap;
    }

    //查询审批记录
    @Transactional(rollbackFor = Exception.class)
    public Object doneView(WorkbenchQueryCriteria workbenchQueryCriteria){
        Map<String,Object> resultMap = new HashMap<>();
        Long commonId = workbenchQueryCriteria.getId();
        WorkflowClassEnum workflowType = workbenchQueryCriteria.getWorkflowType();
        //Integer stepLength = workbenchQueryCriteria.getStepLength();
        WorkflowCommon workflowCommon = commonRepository.findById(commonId).orElseGet(WorkflowCommon::new);
        //workflowCommon.setId(commonId);
        //查找审批记录
        List<WorkflowLog> workflowLogList = logRepository.findByWorkflowCommonAndStepLengthLessThanEqual(workflowCommon,workbenchQueryCriteria.getStepLength());
        Map<String,Object> definePropertiesMap = new HashMap<>();
        WorkflowDefineEveryOne defineEveryOne = everyOneRepository.findByWorkflowCommonIdAndAndStepLength(workflowCommon.getId(),workflowCommon.getStepLength());
        definePropertiesMap.put("stepName",defineEveryOne.getStepName());
        definePropertiesMap.put("stepSeqNum",defineEveryOne.getStepSeqNum());
        //对参数处理  system_approval system_approval
        List<Object> workflowLogMapList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(workflowLogList)){
            //对workflowLogList排序
            List<WorkflowLog> workflowLogSortList = workflowLogList.stream().sorted(Comparator.comparing(WorkflowLog::getCreateTime)).collect(Collectors.toList());
            //处理审批记录
            workflowLogSortList.forEach(item->{
                //BeanMap beanMap = new BeanMap(item);
                Map<String,Object> propertiesMap = new HashMap<>();
                //查询处理者部门
                String organizerName = new String();
                if(item.getStepSeqNum() == 1){
                    User user = userRepository.findByUsername(item.getUsername());
                    organizerName = user.getNickName();
                    propertiesMap.put("accepterName","");
                }else {
                    User accepter = userRepository.findByUsername(item.getUsername());
                    propertiesMap.put("accepterName",accepter.getUsername());
                }
                propertiesMap.put("organizerName",organizerName);

                //翻译agreeFlag
                if(item.getAgreeFlag() != null &&item.getAgreeFlag()){
                    propertiesMap.put("agreeFlagLabel","采纳");
                }else if(item.getAgreeFlag() != null){
                    propertiesMap.put("agreeFlagLabel","不采纳");
                }
                workflowLogMapList.add(ReflectUtil.getObject(item,propertiesMap));
            });
        }
        //.stream().sorted().collect(Collectors.toList());
        resultMap.put("logList",workflowLogMapList);

        switch (workflowType.getCode()){
            case "solicit_opinions":{
                //查找实体
                WorkflowOpinion workflowOpinion = workflowOpinionRepository.findByWorkflowCommon(workflowCommon);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 2){
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,2);
                }else {
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                }
                //feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",workflowOpinion);
                resultMap.put("businessType","opinion");
                break;
            }
            case "system_approval":{

                //查找实体
                WorkflowSystemDocument workflowSystemDocument = workflowSystemDocumentRepository.findByWorkflowCommon(workflowCommon);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 4){
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,4);
                }else {
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                }
                //查找附件
                List<FileAttachDto> fileAttachDtoList = fileUploadService.view(workflowSystemDocument.getSystemDocument());
                resultMap.put("attachList",fileAttachDtoList);
                //Establishment establishment = workflowEstablishment.getEstablishment();
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",ReflectUtil.getObject(workflowSystemDocument,definePropertiesMap));
                resultMap.put("businessType","establishment");
                break;
            }
            case "system_abolition":{
                //查找实体
                WorkflowSystemDocument workflowSystemDocument = workflowSystemDocumentRepository.findByWorkflowCommon(workflowCommon);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 4){
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,4);
                }else {
                    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                }
                //Map<String,Object> prop = new HashMap<>();
                SystemDocument systemDocument = workflowSystemDocument.getSystemDocument();
                List<FileAttachDto> fileAttachDtoList = fileUploadService.view(systemDocument);
                //查找附件
                resultMap.put("attachList",fileAttachDtoList);
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",ReflectUtil.getObject(workflowSystemDocument,definePropertiesMap));
                resultMap.put("businessType","abolition");
                break;
            }
            case "system_plan":{
                //查找实体
                WorkflowRegulationPlan workflowRegulationPlan = workflowRegulationPlanRepository.findByWorkflowCommon(workflowCommon);

                Object entity = ReflectUtil.getObject(workflowRegulationPlan,definePropertiesMap);
                //查找反馈意见
                List<FeedbackOpinion> feedbackOpinionList = new ArrayList<>();
                //步骤4以上都需要看见反馈意见
                //if (workbenchQueryCriteria.getStepSeqNum() == null || workbenchQueryCriteria.getStepSeqNum() > 4){
                //    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,4);
                //}else {
                //    feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                //}
                feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(commonId,workbenchQueryCriteria.getStepSeqNum());
                resultMap.put("feedbackOpinionList",feedbackOpinionList);
                resultMap.put("commonAndBusinessEntity",ReflectUtil.getObject(workflowRegulationPlan,definePropertiesMap));
                resultMap.put("businessType","plan");
            }
        }
        return resultMap;
    }

    public void handleProcess(WorkflowCommon workflowCommon, WorkflowLog workflowLog) {
        //判断下一步
        Integer nowStepNum = workflowCommon.getNowStepNum();
        WorkflowDefineEveryOne defineEveryOne = everyOneRepository.findByWorkflowCommonIdAndAndStepLength(workflowCommon.getId(),nowStepNum);
        //1.判断当前步骤是否处理完毕,节点是否流转
        //String handler = workflowLog.getReminder();

        Boolean agreeFlagNow = workflowLog.getAgreeFlag();
        Integer stepLength = workflowCommon.getStepLength();
        Integer handlerNumNow = workflowCommon.getHandlerNum();
        Boolean preFlag = workflowCommon.getPreFlag();
        String signNow = defineEveryOne.getDisposeWayType().getCode();
        switch (signNow){
            case "add_sign":{//加签 同不同意都往下走
                //直接下一步 nextStep 保存了下一步节点定义，更新了master主表
                nextStep(workflowCommon,workflowLog,nowStepNum+1);
                break;
            }
            case "or_sign":{//或签  有一人处理往下走  抢占式
                if(agreeFlagNow && handlerNumNow != 0){
                    //下一步
                    nextStep(workflowCommon,workflowLog,nowStepNum+1);
                }else if(!agreeFlagNow && handlerNumNow - 1 == 0){  //最后一个人也不同意
                    //需要判断退回，还是待到原节点  按照定义应该待在原节点，所有人不同意会有处理方式
                    nextStep(workflowCommon,workflowLog,nowStepNum-1);
                }
                //流程节点不流转，有一个人处理将其减1
                handlerNumNow -= 1;
                workflowCommon.setHandlerNum(handlerNumNow);
                commonRepository.save(workflowCommon);
                break;
            }
            case "counter_sign":{//会签
                //最前面加上特殊节点处理
                if(nowStepNum == 3){
                    if (preFlag){
                        nextStep(workflowCommon,workflowLog,nowStepNum+1);
                    }else {
                        nextStep(workflowCommon,workflowLog,nowStepNum-1);
                    }
                }else if (handlerNumNow - 1 == 0 && !agreeFlagNow){ //首先判断会签最后一个人同意与否
                    //如果不同意则驳回至上一步
                    nextStep(workflowCommon,workflowLog,nowStepNum-1);
                }else if( handlerNumNow - 1 == 0 && agreeFlagNow){
                    //如果同意，检查所有人
                    List<WorkflowLog> logList = logRepository.findByWorkflowCommonAndStepLength(workflowCommon,stepLength);
                    Boolean isback = false;
                    if (!CollectionUtils.isEmpty(logList)){
                        for(WorkflowLog log:logList){
                            if(!log.getAgreeFlag()){
                                isback = true;
                                break;
                            }
                        }
                        if (isback){
                            nextStep(workflowCommon,workflowLog,nowStepNum-1);
                        }else {
                            nextStep(workflowCommon,workflowLog,nowStepNum+1);
                        }
                    }else {//说明该节点只有一个人处理，并且审批通过，进入下一步
                        nextStep(workflowCommon,workflowLog,nowStepNum+1);
                    }
                }else {
                    //流程节点不流转，有一个人处理将其减1
                    handlerNumNow -= 1;
                    workflowCommon.setHandlerNum(handlerNumNow);
                    commonRepository.save(workflowCommon);
                }
                break;
            }
        }
        //若系统内流程完结需要更改业务实体状态
        if(workflowCommon.getFinishFlag()){
            saveLinkData(workflowCommon);
        }
    }

    /**
     * 保存关联数据
     * @param workflowCommon
     */
    @Async
    public void saveLinkData(WorkflowCommon workflowCommon){
        switch (workflowCommon.getWorkflowType().getCode()){
            case "system_approval":{
                WorkflowSystemDocument workflowSystemDocument = workflowSystemDocumentRepository.findByWorkflowCommon(workflowCommon);
                SystemDocument systemDocument = workflowSystemDocument.getSystemDocument();
                //
                systemDocument.setInstStatus(InstStatusEnum.DONE_ACCEPTED);
                //更改关联计划状态 此处应该为OA审批完结，制度文档入库时更改为完结状态
                systemDocument.getRegulation().setStatus(ProgressStatusEnum.COMPLETE);
                JsonUtil jsonUtil = JsonUtil.getInstance();
                //保存编制依据，释义，风险与目标 查重，不重保存
                List<Map<String,Object>> docAccordingList = (List<Map<String, Object>>) jsonUtil.json2obj(systemDocument.getDocAccording(),List.class);
                if (!CollectionUtils.isEmpty(docAccordingList)){
                    List<Baselibrary> baselibraryList = new ArrayList<>();
                    docAccordingList.forEach(item->{
                        if (StringUtils.isEmpty(MapUtils.getString(item,"systemCode"))){
                            Baselibrary baselibrary = baselibraryRepository.findByBaselibraryName(MapUtils.getString(item,"systemTitle"));
                            baselibrary.setFlag(true);
                            baselibraryList.add(baselibrary);
                        }
                    });
                    if (!CollectionUtils.isEmpty(baselibraryList)){
                        baselibraryRepository.saveAll(baselibraryList);
                    }
                }
                //保存释义
                List<DocExplainDto> docExplainDtoList = jsonUtil.json2list(systemDocument.getDocExplain(),DocExplainDto.class);
                if (!CollectionUtils.isEmpty(docExplainDtoList)){
                    List<DocExplain> docExplainList = new ArrayList<>();
                    docExplainDtoList.forEach(item->{
                        if (ObjectUtils.isEmpty(docExplainRepository.findByValueAndCompanyName(item.getValue(),item.getCompanyName()))){
                            docExplainList.add(new DocExplain(item.getValue(),item.getCompanyName()));
                        }
                    });
                    if (!CollectionUtils.isEmpty(docExplainList)){
                        docExplainRepository.saveAll(docExplainList);
                    }
                }


                //保存风险与目标
                List<DocReplyRiskTargetDto> docReplyRiskTargetDtoList = jsonUtil.json2list(systemDocument.getDocReplyRiskTarget(),DocReplyRiskTargetDto.class);
                if (!CollectionUtils.isEmpty(docReplyRiskTargetDtoList)){
                    List<DocReplyRiskTarget> docReplyRiskTargetList = new ArrayList<>();
                    docReplyRiskTargetDtoList.forEach(item->{
                        if (ObjectUtils.isEmpty(docReplyRiskTargetRepository.findByValueAndCompanyName(item.getValue(),item.getCompanyName()))){
                            docReplyRiskTargetList.add(new DocReplyRiskTarget(item.getValue(),item.getCompanyName()));
                        }
                    });
                    if (!CollectionUtils.isEmpty(docReplyRiskTargetList)){
                        docReplyRiskTargetRepository.saveAll(docReplyRiskTargetList);
                    }
                }

                //Regulation regulation = systemDocument.getRegulation();
                //regulation.setStatus(ProgressStatusEnum.COMPLETE);
                //regulationRepository.save(regulation);
                systemDocumentRepository.save(systemDocument);
                break;
            }
            case "system_abolition":{
                WorkflowSystemDocument workflowSystemDocument = workflowSystemDocumentRepository.findByWorkflowCommon(workflowCommon);
                SystemDocument systemDocument = workflowSystemDocument.getSystemDocument();
                systemDocument.setInstStatus(InstStatusEnum.DONE_ABOLISH);
                systemDocumentRepository.save(systemDocument);
                break;
            }
            case "solicit_opinions":{
                WorkflowOpinion workflowOpinion = workflowOpinionRepository.findByWorkflowCommon(workflowCommon);
                Opinion opinion = workflowOpinion.getOpinion();
                opinion.setStatus(ProgressStatusEnum.COMPLETE);
                opinionRepository.save(opinion);
                break;
            }
            case "system_plan":{
                WorkflowRegulationPlan workflowRegulationPlan = workflowRegulationPlanRepository.findByWorkflowCommon(workflowCommon);
                Regulation regulationPlan = workflowRegulationPlan.getRegulation();
                regulationPlan.setStatus(ProgressStatusEnum.END_OF_APPROVAL);
                regulationRepository.save(regulationPlan);
                break;
            }
        }
    }

    /**
     * 邮件提醒
     * @param objIds
     * @param workflowDisposeObjEnum
     * @param workflowCommon
     */
    @Async
    public void remindUsers(List<String> objIds,WorkflowDisposeObjEnum workflowDisposeObjEnum,WorkflowCommon workflowCommon) {
        EmailVo emailVo = new EmailVo();
        Map<String,Object> paramMap = new HashMap<>();
        emailVo.setTitle(workflowCommon.getWorkflowTitle());

        User drafter = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        //User drafter = userRepository.findById(workflowCommon.getStartUsername()).orElseGet(User::new);
        //paramMap.put("handlerName"," 当前用户（上一步处理人）："+handler.getUsername());
        paramMap.put("drafterName",drafter.getNickName());
        paramMap.put("workflowTypeName",workflowCommon.getWorkflowType().getName());
        emailVo.setParam(paramMap);
        switch (workflowDisposeObjEnum.getCode()){
            case "user":{
                List<String> usernames = new ArrayList<>();
                for(String username:objIds){
                    usernames.add(username);
                }
                List<User> userList = userRepository.findByUsernameIn(usernames);
                List<String> address = new ArrayList<>();
                userList.forEach(item->{
                    address.add(item.getEmail());
                });
                emailVo.setAddress(address);
                emailService.sendProcessAgentEmail(emailVo);
                break;
            }
            case "dept":{

                break;
            }
            case "role":{

                break;
            }
        }
    }

    /**
     * 将流程状态移到下一步
     * @param workflowCommon
     * @param workflowLog
     * @param stepNum   //下一步节点的stepNum
     */

    private void nextStep(WorkflowCommon workflowCommon,WorkflowLog workflowLog,Integer stepNum){
        Integer nowStepNum = workflowCommon.getNowStepNum();
        Integer stepLength = workflowCommon.getStepLength();
        Integer handlerNumNow = workflowCommon.getHandlerNum();
        String handler = workflowLog.getReminder();

        WorkflowDefineEveryOne defineEveryOneNext = new WorkflowDefineEveryOne();
        WorkflowDefine defineNext = defineRepository.findByWorkflowTypeAndStepSeqNum(workflowCommon.getWorkflowType(),stepNum);
        if (!ObjectUtils.isEmpty(defineNext)){
            defineEveryOneNext = defineToDefineEveryMapper.toDto(defineNext);
            Map<String,Object> judgeNextMap = new HashMap<>();
            judgeNextMap.put("defineType",defineNext.getDefineType().getCode());
            judgeNextMap.put("handler",handler);
            judgeNextMap.put("commonId",workflowCommon.getId());
            specialNodeSpecialProcessing(defineEveryOneNext,judgeNextMap);
            stepOperating(workflowCommon, workflowLog, stepLength, handlerNumNow, defineEveryOneNext, stepNum, nowStepNum);
        }else {
            workflowCommon.setFinishFlag(true);
            commonRepository.save(workflowCommon);
            workflowLog.setStepLength(stepLength);
            logRepository.save(workflowLog);
        }
        //switch (nextStepType){
        //    case "next":{
        //        WorkflowDefineEveryOne defineEveryOneNext = new WorkflowDefineEveryOne();
        //        WorkflowDefine defineNext = defineRepository.findByWorkflowTypeAndStepSeqNum(workflowCommon.getWorkflowType(),stepNum);
        //        if (!ObjectUtils.isEmpty(defineNext)){
        //            //finshFlag = false;
        //            defineEveryOneNext = defineToDefineEveryMapper.toDto(defineNext);
        //            //如果下一步人不是确定的，则将分发人放在DisposeObjId，
        //            //if (WorkflowDefineTypeEnum.EXPAND.getCode().equals(defineNext.getDefineType().getCode())){
        //            //    if (StringUtils.isEmpty(handler)){
        //            //        throw new BadRequestException("制度联系人不能为空");
        //            //    }
        //            //    defineEveryOneNext.setDisposeObjType(WorkflowDisposeObjEnum.USER);
        //            //    defineEveryOneNext.setDisposeObjId(","+handler+",");
        //            //}
        //            Map<String,Object> judgeNextMap = new HashMap<>();
        //            judgeNextMap.put("defineType",defineNext.getDefineType());
        //            judgeNextMap.put("handler",handler);
        //            specialNodeSpecialProcessing(defineEveryOneNext,judgeNextMap);
        //            stepOperating(workflowCommon, workflowLog, stepLength, handlerNumNow, defineEveryOneNext, stepNum, nowStepNum);
        //        }else {
        //            workflowCommon.setFinishFlag(true);
        //            commonRepository.save(workflowCommon);
        //            workflowLog.setStepLength(stepLength);
        //            logRepository.save(workflowLog);
        //        }
        //        break;
        //    }
        //    case "previous":{
        //        WorkflowDefineEveryOne defineEveryOnePre = new WorkflowDefineEveryOne();
        //        WorkflowDefine definePre = defineRepository.findByWorkflowTypeAndStepSeqNum(workflowCommon.getWorkflowType(),stepNum);
        //        //finshFlag = false;
        //        defineEveryOnePre = defineToDefineEveryMapper.toDto(definePre);
        //        //如果下一步人不是确定的，则将分发人放在DisposeObjId，
        //        //if (WorkflowDefineTypeEnum.EXPAND.getCode().equals(definePre.getDefineType().getCode())){
        //        //    if(definePre.getStepSeqNum() == 1){
        //        //        handler = definePre.getDisposeObjId();
        //        //        defineEveryOnePre.setDisposeObjType(WorkflowDisposeObjEnum.USER);
        //        //        defineEveryOnePre.setDisposeObjId(handler);
        //        //    }else {
        //        //        if (!StringUtils.isEmpty(handler)){
        //        //            throw new BadRequestException("制度联系人不能为空");
        //        //        }
        //        //        defineEveryOnePre.setDisposeObjType(WorkflowDisposeObjEnum.USER);
        //        //        defineEveryOnePre.setDisposeObjId(","+handler+",");
        //        //    }
        //        //}
        //        Map<String,Object> judgeNextMap = new HashMap<>();
        //        judgeNextMap.put("defineType",definePre.getDefineType());
        //        judgeNextMap.put("handler",handler);
        //        specialNodeSpecialProcessing(defineEveryOnePre,judgeNextMap);
        //
        //        //查找上一步DisposeObjId，清除id
        //        //WorkflowDefineEveryOne defineEveryOnePre = everyOneRepository.findByWorkflowCommonIdAndAndStepLength(workflowCommon.getId(),stepLength - 1);
        //        //defineEveryOnePre.setId(null);
        //
        //
        //        //Integer handlerNumNext = 0;
        //        //String disposeObjId = defineEveryOnePre.getDisposeObjId();
        //        //disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
        //        //List<String> objIds = Arrays.asList(disposeObjId.split(","));
        //        //if (handlerNumNow != 0){  //当handlerNumNow没有归零，并且不是最后一步时
        //        //    handlerNumNext = objIds.size();
        //        //}
        //        //defineEveryOnePre.setStepLength(stepLength + 1);  //添加步长
        //        //defineEveryOnePre.setWorkflowCommonId(workflowCommon.getId());
        //        //workflowCommon.setStepLength(stepLength + 1);
        //        //workflowCommon.setNowStepNum(stepNum);
        //        //workflowCommon.setPreFlag(workflowLog.getAgreeFlag());//记录上一步同意与否
        //        //workflowCommon.setHandlerNum(handlerNumNext);//记录下一节点处理人数量
        //        //commonRepository.save(workflowCommon);
        //        //everyOneRepository.save(defineEveryOnePre);  //保存下一节点定义
        //        //workflowLog.setStepLength(stepLength);
        //        //logRepository.save(workflowLog);
        //        //remindUsers(objIds,defineEveryOnePre.getDisposeObjType(),workflowCommon);
        //        stepOperating(workflowCommon, workflowLog, stepLength, handlerNumNow, defineEveryOnePre, stepNum, nowStepNum);
        //        break;
        //    }
        //    //case "jump":{
        //    //    WorkflowDefineEveryOne defineEveryOneJump = new WorkflowDefineEveryOne();
        //    //    WorkflowDefine defineJump = defineRepository.findByWorkflowTypeAndStepSeqNum(workflowCommon.getWorkflowType(),stepNum);
        //    //    //finshFlag = false;
        //    //    defineEveryOneJump = defineToDefineEveryMapper.toDto(defineJump);
        //    //    //如果下一步人不是确定的，则将分发人放在DisposeObjId，
        //    //    //if (WorkflowDefineTypeEnum.EXPAND.getCode().equals(defineJump.getDefineType().getCode())){
        //    //    //    if (!StringUtils.isEmpty(handler)){
        //    //    //        throw new BadRequestException("制度联系人不能为空");
        //    //    //    }
        //    //    //    defineEveryOneJump.setDisposeObjType(WorkflowDisposeObjEnum.USER);
        //    //    //    defineEveryOneJump.setDisposeObjId(","+handler+",");
        //    //    //}
        //    //    Map<String,Object> judgeNextMap = new HashMap<>();
        //    //    judgeNextMap.put("defineType",defineJump.getDefineType());
        //    //    judgeNextMap.put("handler",handler);
        //    //    specialNodeSpecialProcessing(defineEveryOneJump,judgeNextMap);
        //    //    stepOperating(workflowCommon, workflowLog, stepLength, handlerNumNow, defineEveryOneJump, stepNum, nowStepNum);
        //    //    break;
        //    //}
        //}
    }

    /**
     * 移动步骤进行的操作
     * @param workflowCommon  //要进行设置的当前流程
     * @param workflowLog     //流程将要保存的日志
     * @param stepLength      //步长
     * @param handlerNumNow   //当前步骤需要处理的人数
     * @param defineEveryOneNext  //下一个节点定义
     * @param stepNum                  //主流程下一个节点的nowStepNum
     * @param nowStepNum         //主流程当前节点的nowStepNum
     */
    @Async
    public void stepOperating(WorkflowCommon workflowCommon, WorkflowLog workflowLog, Integer stepLength, Integer handlerNumNow, WorkflowDefineEveryOne defineEveryOneNext, Integer stepNum, Integer nowStepNum) {
        Integer handlerNumNext = 0;
        String disposeObjId = defineEveryOneNext.getDisposeObjId();
        disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
        List<String> objIds = Arrays.asList(disposeObjId.split(","));
        if (handlerNumNow != 0){  //当handlerNumNow没有归零，并且不是最后一步时
            handlerNumNext = objIds.size();
        }
        defineEveryOneNext.setStepLength(stepLength + 1);  //添加步长
        defineEveryOneNext.setWorkflowCommonId(workflowCommon.getId());
        workflowCommon.setStepLength(stepLength + 1);
        workflowCommon.setNowStepNum(stepNum);
        workflowCommon.setPreFlag(workflowLog.getAgreeFlag());//记录上一步同意与否
        workflowCommon.setHandlerNum(handlerNumNext);//记录下一节点处理人数量
        commonRepository.save(workflowCommon);
        everyOneRepository.save(defineEveryOneNext);  //保存下一节点定义
        workflowLog.setStepLength(stepLength);
        logRepository.save(workflowLog);
        remindUsers(objIds,defineEveryOneNext.getDisposeObjType(),workflowCommon);
    }

    /**
     * 特殊节点一些特殊处理
     * @param workflowDefineEveryOne  //将要修改的下一步节点定义
     * @param paramMap
     */
    public void specialNodeSpecialProcessing(WorkflowDefineEveryOne workflowDefineEveryOne,Map<String,Object> paramMap){
        Map<String,Object> resultMap = new HashMap<>();
        //1.流程类型  2.流程节点  3.expend类型
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        User upUser = new User();
        String defineType = MapUtils.getString(paramMap,"defineType");
        String handler = MapUtils.getString(paramMap,"handler");
        Long commonId = MapUtils.getLong(paramMap,"commonId");
        if(WorkflowDefineTypeEnum.EXPAND.getCode().equals(defineType)){
            if(workflowDefineEveryOne.getStepSeqNum() == 1){
                handler = workflowDefineEveryOne.getDisposeObjId();
                workflowDefineEveryOne.setDisposeObjType(WorkflowDisposeObjEnum.USER);
                workflowDefineEveryOne.setDisposeObjId(handler);
            }else {
                if (workflowDefineEveryOne.getStepSeqNum() == 3 && (WorkflowClassEnum.SYSTEM.getCode().equals(workflowDefineEveryOne.getWorkflowType().getCode())
                        || WorkflowClassEnum.ABOLITION.getCode().equals(workflowDefineEveryOne.getWorkflowType().getCode()))){
                    handler = everyOneRepository.findByWorkflowCommonIdAndAndStepLength(commonId,1).getDisposeObjId();
                }
                if (StringUtils.isEmpty(handler)){
                    throw new BadRequestException("制度联系人不能为空");
                }
                workflowDefineEveryOne.setDisposeObjType(WorkflowDisposeObjEnum.USER);
                workflowDefineEveryOne.setDisposeObjId(","+handler+",");
            }
        }else if(WorkflowDefineTypeEnum.EXPAND1.getCode().equals(defineType)){
            upUser = userRepository.findUserByDeptIdAndJobName(user.getDept().getId(),"部门负责人");
            if (ObjectUtils.isEmpty(upUser) || StringUtils.isEmpty(upUser.getUsername())){
                throw new BadRequestException("部门负责人为空");
            }
            workflowDefineEveryOne.setDisposeObjType(WorkflowDisposeObjEnum.USER);
            workflowDefineEveryOne.setDisposeObjId(","+upUser.getUsername()+",");
        }else if(WorkflowDefineTypeEnum.EXPAND2.getCode().equals(defineType)){
            upUser = userRepository.findUserByDeptIdAndJobName(user.getDept().getId(),"处长");
            if (StringUtils.isEmpty(upUser.getUsername())){
                throw new BadRequestException("处长为空");
            }
            workflowDefineEveryOne.setDisposeObjType(WorkflowDisposeObjEnum.USER);
            workflowDefineEveryOne.setDisposeObjId(","+upUser.getUsername()+",");
        }
        //return resultMap;
    }


    public Object getProcessPath(Long id) {
        WorkflowCommon workflowCommon = commonRepository.findById(id).orElseGet(WorkflowCommon::new);
        List<WorkflowDefine> workflowDefineList = defineRepository.findAllByWorkflowType(workflowCommon.getWorkflowType());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("stepList",workflowDefineList.stream().sorted(
                Comparator.comparing(WorkflowDefine::getStepSeqNum))
                .collect(Collectors.toList()));
        resultMap.put("nowStep",defineRepository.findByWorkflowTypeAndStepSeqNum(workflowCommon.getWorkflowType(),workflowCommon.getNowStepNum()));
        return resultMap;
    }

    public List getExcelList(List<WorkflowCommon> workflowCommonList){
        //存放结果集
        List<Map<String,Object>> resultList = new ArrayList<>();
        //存放查询log实体
        List<WorkflowLog> resultLogList = new ArrayList<>();
        resultLogList = logRepository.findByWorkflowCommonIn(workflowCommonList);
        Integer i = 1;
        for(WorkflowLog item:resultLogList){
            Map<String,Object> map = new HashMap<>();
            map.put("no",i);
            map.put("workflowTitle",item.getWorkflowCommon().getWorkflowTitle());
            map.put("workflowNum",item.getWorkflowCommon().getWorkflowNum());
            map.put("stepName",item.getStepName());
            map.put("accepterName",userRepository.findByUsername(item.getUsername()).getNickName());
            map.put("organizerName",userRepository.findByUsername(item.getWorkflowCommon().getStartUsername()).getNickName());
            if (item.getAgreeFlag()!= null && item.getAgreeFlag()) {
                map.put("agreeFlag", "采纳");
            } else {
                map.put("agreeFlag", "不采纳");
            }
            map.put("opinion",item.getOpinion());
            map.put("createTime",item.getCreateTime());
            List<FeedbackOpinion> feedbackOpinionList = feedbackOpinionRepository.findByWorkflowCommonIdAndStepSeqNum(item.getWorkflowCommon().getId(),item.getStepSeqNum());
            if(!CollectionUtils.isEmpty(feedbackOpinionList)){
                StringBuilder feedback = new StringBuilder();
                feedbackOpinionList.forEach(it->{
                    feedback.append("第").append(it.getChapter()).append("节: ")
                            .append("   ").append(it.getOpinionContent());
                });
                feedback.append("拒绝理由: ").append(feedbackOpinionList.get(0).getRejectReason());
                map.put("feedback",feedback.toString());
            }else {
                map.put("feedback","N/A");
            }
            resultList.add(map);
            i+=1;
        }
        return resultList;
    }
}
