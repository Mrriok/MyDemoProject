///**
// * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
// * Use is subject to license terms.<br>
// * <p>
// * 在此处填写文件说明
// */
//package com.zony.app.service;
//
//import com.zony.app.domain.*;
//import com.zony.app.domain.vo.WorkbenchTestVo;
//import com.zony.app.domain.vo.WorkbenchVo;
//import com.zony.app.enums.WorkflowClassEnum;
//import com.zony.app.enums.WorkflowDefineTypeEnum;
//import com.zony.app.enums.WorkflowDisposeObjEnum;
//import com.zony.app.repository.*;
//import com.zony.app.service.criteria.WorkbenchQueryCriteria;
//import com.zony.app.service.dto.WorkbenchTodoTestDto;
//import com.zony.app.service.mapstruct.DefineToDefineEveryMapper;
//import com.zony.app.service.mapstruct.NodeToNodeEveryMapper;
//import com.zony.app.service.mapstruct.OpinionMapper;
//import com.zony.app.service.mapstruct.RegulationMapper;
//import com.zony.app.utils.ReflectUtil;
//import com.zony.common.exception.BadRequestException;
//import com.zony.common.service.EmailService;
//import com.zony.common.utils.JsonUtil;
//import com.zony.common.utils.SecurityUtils;
//import com.zony.common.utils.StringUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.sql.Timestamp;
//import java.util.*;
//
///**
// * 填写功能简述.
// * <p>填写详细说明.<br>
// * @version v1.0
// * @author MrriokChen
// * @date 2020/8/11 -16:19
// */
//@Service
//@RequiredArgsConstructor
//@CacheConfig(cacheNames = "workbenchTest")
//public class WorkbenchTestService {
//
//    private final UserRepository userRepository;
//    private final WorkflowCommonRepository commonRepository;
//    //private final WorkbenchRepository workbenchRepository;
//    private final WorkflowDefineRepository defineRepository;
//    private final WorkflowDefineEveryOneRepository everyOneRepository;
//    private final WorkflowLogRepository logRepository;
//    private final WorkflowOpinionRepository workflowOpinionRepository;
//    private final WorkflowEstablishmentRepository workflowEstablishmentRepository;
//    private final DefineToDefineEveryMapper defineToDefineEveryMapper;
//    private final FeedbackOpinionRepository feedbackOpinionRepository;
//    private final WorkflowSystemDocumentRepository workflowSystemDocumentRepository;
//    private final WorkflowRegulationPlanRepository workflowRegulationPlanRepository;
//    private final EstablishmentRepository establishmentRepository;
//    private final OpinionRepository opinionRepository;
//    private final SystemDocumentRepository systemDocumentRepository;
//    private final RegulationRepository regulationRepository;
//    private final EmailService emailService;
//    private final FileUploadService fileUploadService;
//    private final RegulationMapper regulationMapper;
//    private final OpinionMapper opinionMapper;
//    @PersistenceContext
//    private EntityManager em;
//
//    private static String NEXT = "next";
//    private static String PREVIOUS = "previous";
//    private static String JUMP = "jump";
//
//    //新建一个流程
//    @Transactional(rollbackFor = Exception.class)
//    public void createProcess(WorkbenchVo workbenchVo){
//
//        String businessType = workbenchVo.getBusinessType();
//        WorkflowCommon workflowCommon = new WorkflowCommon();
//        if (StringUtils.isEmpty(businessType)){
//            throw new BadRequestException("必填字段不能为空！");
//        }
//        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
//        Integer stepLength = 1;
//        WorkflowSystemDocument workflowSystemDocument = new WorkflowSystemDocument();
//        SystemDocument systemDocument = workbenchVo.getSystemDocument();
//        if(ObjectUtils.isEmpty(systemDocument)){
//            throw new BadRequestException("必填字段不能为空！");
//        }
//        workflowSystemDocument.setSystemDocument(systemDocument);
//        //查询流程基本定义
//        WorkflowDefine define1 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.SYSTEM,1);
//        WorkflowDefine define2 = defineRepository.findByWorkflowTypeAndStepSeqNum(WorkflowClassEnum.SYSTEM,2);
//        define1.setDisposeObjType(WorkflowDisposeObjEnum.USER);
//        define1.setDisposeObjId("," + user.getUsername() + ",");
//        //将每步流程定义存放到everyOne
//        WorkflowDefineEveryOne defineEveryOne1 = defineToDefineEveryMapper.toDto(define1);
//        WorkflowDefineEveryOne defineEveryOne2 = defineToDefineEveryMapper.toDto(define2);
//        defineEveryOne1.setStepLength(stepLength);
//        defineEveryOne2.setStepLength(stepLength + 1);
//        String disposeObjId = defineEveryOne2.getDisposeObjId();
//        disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
//        List<String> objIds = Arrays.asList(disposeObjId.split(","));
//        //设置流程基本信息
//        workflowCommon.setWorkflowNum(systemDocument.getSystemCode());
//        workflowCommon.setWorkflowTitle(systemDocument.getSystemTitle());
//        workflowCommon.setWorkflowType(WorkflowClassEnum.SYSTEM);
//        workflowCommon.setStartUsername(user.getUsername());
//        workflowCommon.setStepLength(stepLength);
//        workflowCommon.setNowStepNum(1);
//        workflowCommon.setFinishFlag(false);
//        workflowCommon.setPreFlag(true);
//        workflowCommon.setHandlerNum(1);
//        WorkflowCommon workflowCommon1 = commonRepository.save(workflowCommon);
//        defineEveryOne1.setWorkflowCommonId(workflowCommon1.getId());
//        defineEveryOne2.setWorkflowCommonId(workflowCommon1.getId());
//        everyOneRepository.save(defineEveryOne1);
//        everyOneRepository.save(defineEveryOne2);
//        //设置发起记录
//        //saveLog(workflowCommon1,defineEveryOne1,user.getUsername(),null,null,null);
//
//        workflowCommon1.setHandlerNum(objIds.size());
//        workflowCommon1.setNowStepNum(2);
//        workflowCommon1.setFinishFlag(false);
//        workflowCommon1.setStepLength(stepLength + 1);
//        commonRepository.save(workflowCommon1);
//        workflowSystemDocument.setWorkflowCommon(workflowCommon1);
//        workflowSystemDocumentRepository.save(workflowSystemDocument);
//        //remindUsers(objIds,WorkflowDisposeObjEnum.USER,workflowCommon1);
//        //establishmentRepository.save(establishment);
//        break;
//    }
//    private void saveLog(WorkflowMaster workflowMaster,WorkflowNodeEveryOne nodeEveryOne,Long username,Boolean agreeFlag,String reminder,String opinion){
//        WorkflowLogTest workflowLogTest = new WorkflowLogTest();
//        //workflowCommon.setNowStepNum(nodeEveryOne.getId());
//        workflowLogTest.setStepSeqNum(nodeEveryOne.getStepSeqNum());
//        workflowLogTest.setStepName(nodeEveryOne.getStepName());
//        workflowLogTest.setUsername(username);
//        workflowLogTest.setAgreeFlag(agreeFlag);
//        workflowLogTest.setReminder(reminder);
//        workflowLogTest.setOpinion(opinion);
//        workflowLogTest.setWorkflowMaster(workflowMaster);
//        workflowLogTest.setStepLength(workflowMaster.getStepLength());
//        logTestRepository.save(workflowLogTest);
//    }
//
//    //查询待办详情
//    @Transactional(rollbackFor = Exception.class)
//    public Object todoView(WorkbenchQueryCriteria workbenchQueryCriteria){
//        Map<String,Object> resultMap = new HashMap<>();
//        Long masterId = workbenchQueryCriteria.getId();
//        WorkflowClassEnum workflowType = workbenchQueryCriteria.getWorkflowType();
//        Integer stepLength = workbenchQueryCriteria.getStepLength();
//        WorkflowMaster workflowMaster = new WorkflowMaster();
//        workflowMaster.setId(masterId);
//        //查找审批记录
//        List<WorkflowLogTest> workflowLogTestList = logTestRepository.findByWorkflowMaster(workflowMaster);
//        WorkflowNodeEveryOne nodeEveryOne = everyOneRepository.findByWorkflowMasterIdAndAndStepLength(masterId,stepLength);
//
//        switch (workflowType.getCode()){
//            case "solicit_opinions":{
//                //if (workbenchQueryCriteria.getStepSeqNum() == 1){
//                //    workflowOpinionRepository.findById(workbenchQueryCriteria.getId()).orElseThrow(() -> new EntityNotFoundException(WorkflowOpinion.class,"id ",workbenchQueryCriteria.getId().toString()));
//                //}
//                break;
//            }
//            case "system_approval":{
//                //对参数处理  system_approval system_approval
//                List<Object> workflowLogMapList = new ArrayList<>();
//                if (!CollectionUtils.isEmpty(workflowLogTestList)){
//                    //处理审批记录
//                    workflowLogTestList.forEach(item->{
//                        //BeanMap beanMap = new BeanMap(item);
//                        Map<String,Object> propertiesMap = new HashMap<>();
//                        //查询处理者部门
//                        String organizerName = new String();
//                        if(item.getStepSeqNum() == 1){
//                            User user = userRepository.findById(item.getUsername()).orElseGet(User::new);
//                            organizerName = user.getUsername();
//                            propertiesMap.put("accepterName","");
//                        }else {
//                            User accepter = userRepository.findById(item.getUsername()).orElseGet(User::new);
//                            propertiesMap.put("accepterName",accepter.getUsername());
//                        }
//                        propertiesMap.put("organizerName",organizerName);
//
//                        //翻译agreeFlag
//                        if(item.getAgreeFlag() != null &&item.getAgreeFlag()){
//                            propertiesMap.put("agreeFlagLabel","采纳");
//                        }else if(item.getAgreeFlag() != null){
//                            propertiesMap.put("agreeFlagLabel","不采纳");
//                        }
//                        workflowLogMapList.add(ReflectUtil.getObject(item,propertiesMap));
//                    });
//                }
//                resultMap.put("logList",workflowLogMapList);
//                //查找实体
//                WorkflowEstablishmentTest workflowEstablishmentTest = workflowEstablishmentTestRepository.findByWorkflowMaster(workflowMaster);
//
//                //查找反馈意见
//                List<WorkflowFeedback> feedbackList = new ArrayList<>();
//                if (workbenchQueryCriteria.getStepSeqNum() == null){
//                    feedbackList = feedbackRepository.findByWorkflowMasterIdAndStepSeqNum(masterId,4);
//                }else {
//                    feedbackList = feedbackRepository.findByWorkflowMasterIdAndStepSeqNum(masterId,workbenchQueryCriteria.getStepSeqNum());
//                }
//                resultMap.put("feedbackOpinionList",feedbackList);
//                //String fileId = workflowEstablishment.getEstablishment().getAttachmentFileIds();
//                //List<String> fileIdList = Arrays.asList(fileId.split(","));
//                //SystemDocumentQueryCrietria crietria = new SystemDocumentQueryCrietria();
//                //crietria.setSystemCode(workflowEstablishment.getEstablishment().getInstCode());
//                ////systemDocumentService.queryAll(crietria);
//                //for (String id :fileIdList){
//                //
//                //}
//                resultMap.put("commonAndBusinessEntity",workflowEstablishmentTest);
//                resultMap.put("businessType","establishment");
//                break;
//            }
//        }
//        return resultMap;
//    }
//
//
//    public List<WorkbenchTodoTestDto> queryTodoList(WorkbenchQueryCriteria queryCriteria) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//
//        //构建sql 主体
//        //order by common.id desc
//        StringBuilder sql = new StringBuilder()
//        .append("select new com.zony.app.service.dto.WorkbenchTodoTestDto(master.id,master.workflowType,master.workflowNum,master.workflowTitle,master.nowStepNum,master.startUsername,master.finishFlag,master.createTime,nodeEveryOne.stepName,nodeEveryOne.stepSeqNum,nodeEveryOne.disposeObjId,nodeEveryOne.disposeObjType,nodeEveryOne.disposeWayType,user.username)" +
//                " from WorkflowMaster master,WorkflowNodeEveryOne nodeEveryOne,User user where master.id = nodeEveryOne.workflowMasterId and master.nowStepNum = nodeEveryOne.stepSeqNum and master.stepLength = nodeEveryOne.stepLength and master.finishFlag = 0 and user.id = master.startUsername");
//        Map<String,Object> paramMap = new HashMap<>();
//        //构建sql查询  循环版 还需判断那个表内，以及等于还是like,
//        Field[] fields = queryCriteria.getClass().getDeclaredFields();
//        for(int i = 0;i<fields.length;i++){
//            String name = fields[i].getName();
//            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
//            Method m = queryCriteria.getClass().getMethod(methodName);
//            Object value = m.invoke(queryCriteria);
//            if(value != null){
//                //node
//                if(name.equals("disposeObjId") || name.equals("stepName")){
//                    sql.append(" and nodeEveryOne."+name + " like :" +name);
//                }else if(name.equals("disposeObjType") || name.equals("disposeWayType") ||  name.equals("stepSeqNum")){
//                    sql.append(" and nodeEveryOne."+name + " = :" +name);
//                }
//                //master
//                else if(name.equals("workflowNum") || name.equals("workflowTitle") ) {
//                    sql.append(" and master." + name + " like :" +name);
//                } else if(name.equals("id") || name.equals("workflowType") ||  name.equals("nowStepNum") || name.equals("startUsername")) {
//                    sql.append(" and master." + name + " = :" +name);
//                }else if(name.equals("initTime")) {
//                    sql.append(" and master." + "initTime" + " between :startTime and :endTime");
//                }else {//startUsernameList
//                    sql.append(" and master."+name + " in :(" +name+")");
//                }
//                paramMap.put(name,value);
//            }
//        }
//
//        //添加参数 em.createQuery(sql.toString(),Map.class); 返回map
//        Query query = em.createQuery(sql.toString());
//        for (Map.Entry<String,Object> entry: paramMap.entrySet()){
//            String key = entry.getKey();
//            if ("disposeObjId".equals(key)){
//                StringBuilder value = new StringBuilder("%,").append(entry.getValue().toString()).append(",%");
//                query.setParameter(key,value.toString());
//            }else if("stepName".equals(key)||"workflowNum".equals(key)||"workflowTitle".equals(key)){
//                StringBuilder value = new StringBuilder("%").append(entry.getValue().toString()).append("%");
//                query.setParameter(key,value.toString());
//            } else if("initTime".equals(key)){
//                List<Timestamp> initTimeList = (List<Timestamp>) entry.getValue();
//                query.setParameter("startTime",initTimeList.get(0));
//                query.setParameter("endTime",initTimeList.get(1));
//            }else {
//                query.setParameter(entry.getKey(),entry.getValue());
//            }
//        }
//        //query.setFirstResult(startIndex).setMaxResults(endIndex);
//        return query.getResultList();
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void updateWorkflow(WorkbenchVo workbenchVo) {
//        WorkflowLog workflowLog =  workbenchVo.getWorkflowLog();
//        List<FeedbackOpinion> feedbackOpinionList = workbenchVo.getFeedbackOpinionList();
//        String businessType = workbenchVo.getBusinessType();
//
//    }
//
//
//    public void handleProcess(WorkflowMaster workflowMaster,WorkflowLogTest workflowLogTest) {
//
//        //判断下一步
//        Integer nowStepNum = workflowMaster.getNowStepNum();
//        WorkflowNodeEveryOne nodeEveryOneNow = everyOneRepository.findByWorkflowMasterIdAndAndStepLength(workflowMaster.getId(),nowStepNum);
//        //1.判断当前步骤是否处理完毕,节点是否流转
//        String handler = workflowLogTest.getReminder();
//        Integer stepLength = workflowMaster.getStepLength();
//        Integer handlerNumNow = workflowMaster.getHandlerNum();
//        Boolean agreeFlagNow = workflowLogTest.getAgreeFlag();
//        Boolean preFlag = workflowMaster.getPreFlag();
//        String signNow = nodeEveryOneNow.getDisposeWayType().getCode();
//        switch (signNow){
//            case "add_sign":{//加签 同不同意都往下走
//                //直接下一步 nextStep 保存了下一步节点定义，更新了master主表
//                nextStep(workflowMaster,workflowLogTest,NEXT,null);
//                break;
//            }
//            case "or_sign":{//或签  有一人处理往下走  抢占式
//                if(agreeFlagNow && handlerNumNow != 0){
//                    //下一步
//                    nextStep(workflowMaster,workflowLogTest,NEXT,null);
//                }else if(!agreeFlagNow && handlerNumNow - 1 == 0){  //最后一个人也不同意
//                    //需要判断退回，还是待到原节点  按照定义应该待在原节点，所有人不同意会有处理方式
//                    nextStep(workflowMaster,workflowLogTest,PREVIOUS,null);
//                }
//                //流程节点不流转，有一个人处理将其减1
//                handlerNumNow -= 1;
//                workflowMaster.setHandlerNum(handlerNumNow);
//                masterRepository.save(workflowMaster);
//                break;
//            }
//            case "counter_sign":{//会签
//                //最前面加上特殊节点处理
//                if(nowStepNum == 3){
//                    if (preFlag){
//                        nextStep(workflowMaster,workflowLogTest,NEXT,null);
//                    }else {
//                        nextStep(workflowMaster,workflowLogTest,PREVIOUS,null);
//                    }
//                }else if (handlerNumNow - 1 == 0 && !agreeFlagNow){ //首先判断会签最后一个人同意与否
//                    //如果不同意则驳回至上一步
//                    nextStep(workflowMaster,workflowLogTest,PREVIOUS,null);
//                }else if( handlerNumNow - 1 == 0 && agreeFlagNow){
//                    //如果同意，检查所有人
//                    List<WorkflowLogTest> logTestList = logTestRepository.findByWorkflowMasterAndStepLength(workflowMaster,stepLength);
//                    Boolean isback = false;
//                    if (!CollectionUtils.isEmpty(logTestList)){
//                        for(WorkflowLogTest log:logTestList){
//                            if(!log.getAgreeFlag()){
//                                isback = true;
//                                break;
//                            }
//                        }
//                        if (isback){
//                            nextStep(workflowMaster,workflowLogTest,PREVIOUS,null);
//                        }else {
//                            nextStep(workflowMaster,workflowLogTest,NEXT,null);
//                        }
//                    }else {//说明该节点只有一个人处理，并且审批通过，进入下一步
//                        nextStep(workflowMaster,workflowLogTest,NEXT,null);
//                    }
//                }
//                //流程节点不流转，有一个人处理将其减1
//                handlerNumNow -= 1;
//                workflowMaster.setHandlerNum(handlerNumNow);
//                masterRepository.save(workflowMaster);
//                break;
//            }
//        }
//    }
//
//    private void remindUsers(List<String> objIds,WorkflowDisposeObjEnum workflowDisposeObjEnum) {
//        switch (workflowDisposeObjEnum.getCode()){
//            case "user":{
//
//                break;
//            }
//            case "dept":{
//
//                break;
//            }
//            case "role":{
//
//                break;
//            }
//        }
//    }
//
//
//    private void nextStep(WorkflowMaster workflowMaster,WorkflowLogTest logTest,String nextStepType,Integer stepNum){
//        Integer nowStepNum = workflowMaster.getNowStepNum();
//        Integer stepLength = workflowMaster.getStepLength();
//        Integer handlerNumNow = workflowMaster.getHandlerNum();
//        String handler = logTest.getReminder();
//        switch (nextStepType){
//            case "next":{
//                WorkflowNodeEveryOne nodeEveryOneNext = new WorkflowNodeEveryOne();
//                WorkflowNode nodeNext = nodeRepository.findByWorkflowTypeAndStepSeqNum(workflowMaster.getWorkflowType(),nowStepNum + 1);
//                if (!ObjectUtils.isEmpty(nodeNext)){
//                    //finshFlag = false;
//                    nodeEveryOneNext = nodeToNodeEveryMapper.toDto(nodeNext);
//                    //如果下一步人不是确定的，则将分发人放在DisposeObjId，
//                    if (WorkflowDefineTypeEnum.EXPAND.getCode().equals(nodeNext.getDefineType().getCode())){
//                        if (!StringUtils.isEmpty(handler)){
//                            throw new BadRequestException("制度联系人不能为空");
//                        }
//                        nodeEveryOneNext.setDisposeObjType(WorkflowDisposeObjEnum.USER);
//                        nodeEveryOneNext.setDisposeObjId(","+handler+",");
//                    }
//                    Integer handlerNumNext = 0;
//                    String disposeObjId = nodeEveryOneNext.getDisposeObjId();
//                    disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
//                    List<String> objIds = Arrays.asList(disposeObjId.split(","));
//                    if (handlerNumNow != 0){  //当handlerNumNow没有归零，并且不是最后一步时
//                        handlerNumNext = objIds.size();
//                    }
//                    nodeEveryOneNext.setStepLength(stepLength + 1);  //添加步长
//                    everyOneRepository.save(nodeEveryOneNext);  //保存下一节点定义
//                    workflowMaster.setStepLength(stepLength + 1);
//                    workflowMaster.setNowStepNum(nowStepNum + 1);
//                    workflowMaster.setPreFlag(logTest.getAgreeFlag());//记录上一步同意与否
//                    workflowMaster.setHandlerNum(handlerNumNext);//记录下一节点处理人数量
//                    masterRepository.save(workflowMaster);
//                    remindUsers(objIds,nodeEveryOneNext.getDisposeObjType());
//                }else {
//                    workflowMaster.setFinishFlag(true);
//                    masterRepository.save(workflowMaster);
//                }
//                break;
//            }
//            case "previous":{
//                WorkflowNodeEveryOne nodeEveryOnePre = new WorkflowNodeEveryOne();
//                WorkflowNode nodePre = nodeRepository.findByWorkflowTypeAndStepSeqNum(workflowMaster.getWorkflowType(),nowStepNum - 1);
//                //finshFlag = false;
//                nodeEveryOnePre = nodeToNodeEveryMapper.toDto(nodePre);
//                //如果下一步人不是确定的，则将分发人放在DisposeObjId，
//                if (WorkflowDefineTypeEnum.EXPAND.getCode().equals(nodePre.getDefineType().getCode())){
//                    if (!StringUtils.isEmpty(handler)){
//                        throw new BadRequestException("制度联系人不能为空");
//                    }
//                    nodeEveryOnePre.setDisposeObjType(WorkflowDisposeObjEnum.USER);
//                    nodeEveryOnePre.setDisposeObjId(","+handler+",");
//                }
//                Integer handlerNumNext = 0;
//                String disposeObjId = nodeEveryOnePre.getDisposeObjId();
//                disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
//                List<String> objIds = Arrays.asList(disposeObjId.split(","));
//                if (handlerNumNow != 0){  //当handlerNumNow没有归零，并且不是最后一步时
//                    handlerNumNext = objIds.size();
//                }
//                nodeEveryOnePre.setStepLength(stepLength + 1);  //添加步长
//                everyOneRepository.save(nodeEveryOnePre);  //保存下一节点定义
//                workflowMaster.setStepLength(stepLength + 1);
//                workflowMaster.setNowStepNum(nowStepNum - 1);
//                workflowMaster.setPreFlag(logTest.getAgreeFlag());//记录上一步同意与否
//                workflowMaster.setHandlerNum(handlerNumNext);//记录下一节点处理人数量
//                masterRepository.save(workflowMaster);
//                remindUsers(objIds,nodeEveryOnePre.getDisposeObjType());
//                break;
//            }
//            case "jump":{
//                WorkflowNodeEveryOne nodeEveryOneJump = new WorkflowNodeEveryOne();
//                WorkflowNode nodeJump = nodeRepository.findByWorkflowTypeAndStepSeqNum(workflowMaster.getWorkflowType(),stepNum);
//                //finshFlag = false;
//                nodeEveryOneJump = nodeToNodeEveryMapper.toDto(nodeJump);
//                //如果下一步人不是确定的，则将分发人放在DisposeObjId，
//                if (WorkflowDefineTypeEnum.EXPAND.getCode().equals(nodeJump.getDefineType().getCode())){
//                    if (!StringUtils.isEmpty(handler)){
//                        throw new BadRequestException("制度联系人不能为空");
//                    }
//                    nodeEveryOneJump.setDisposeObjType(WorkflowDisposeObjEnum.USER);
//                    nodeEveryOneJump.setDisposeObjId(","+handler+",");
//                }
//                Integer handlerNumNext = 0;
//                String disposeObjId = nodeEveryOneJump.getDisposeObjId();
//                disposeObjId = disposeObjId.substring(1,disposeObjId.length() - 1);
//                List<String> objIds = Arrays.asList(disposeObjId.split(","));
//                if (handlerNumNow != 0){  //当handlerNumNow没有归零，并且不是最后一步时
//                    handlerNumNext = objIds.size();
//                }
//                nodeEveryOneJump.setStepLength(stepLength + 1);  //添加步长
//                everyOneRepository.save(nodeEveryOneJump);  //保存下一节点定义
//                workflowMaster.setStepLength(stepLength + 1);
//                workflowMaster.setNowStepNum(stepNum);
//                workflowMaster.setPreFlag(logTest.getAgreeFlag());//记录上一步同意与否
//                workflowMaster.setHandlerNum(handlerNumNext);//记录下一节点处理人数量
//                masterRepository.save(workflowMaster);
//                remindUsers(objIds,nodeEveryOneJump.getDisposeObjType());
//                break;
//            }
//        }
//    }
//
//}
