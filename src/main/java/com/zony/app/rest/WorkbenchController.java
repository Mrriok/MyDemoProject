/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import cn.hutool.core.map.MapUtil;
import com.zony.app.domain.*;
import com.zony.app.domain.vo.SystemDocumentFormVo;
import com.zony.app.domain.vo.WorkbenchVo;
import com.zony.app.enums.WorkflowDisposeObjEnum;
import com.zony.app.repository.*;
import com.zony.app.service.EstablishmentService;
import com.zony.app.service.SystemDocumentService;
import com.zony.app.service.WorkbenchService;
import com.zony.app.service.WorkflowDefineService;
import com.zony.app.service.criteria.WorkbenchLogQueryCriteria;
import com.zony.app.service.criteria.WorkbenchQueryCriteria;
import com.zony.app.service.dto.WorkbenchDoneDto;

import com.zony.app.service.dto.WorkbenchTodoDto;
import com.zony.app.utils.ExcelUtil;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.SecurityUtils;
import com.zony.common.utils.StringUtils;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/11 -16:16
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "工作台")
@RequestMapping("/api/workbench")
public class WorkbenchController {
    private final WorkbenchService workbenchService;
    private final WorkflowDefineService workflowDefineService;
    private final WorkflowLogRepository workflowLogRepository;
    private final UserRepository userRepository;
    private final FeedbackOpinionRepository feedbackOpinionRepository;
    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentService establishmentService;
    private final WorkflowCommonRepository workflowCommonRepository;
    private final OpinionRepository opinionRepository;
    private final SystemDocumentService systemDocumentService;
    private final SystemDocumentRepository systemDocumentRepository;
    private final RegulationRepository regulationRepository;
    private final DictDetailRepository dictDetailRepository;
    private final DictRepository dictRepository;
    @Log("查询待办事项列表")
    @ApiOperation("查询待办事项列表")
    @PostMapping("/todoList")
    @PreAuthorize("@el.check('workbench:todoList')")
    public ResponseEntity<Object> queryTodo(@RequestBody WorkbenchQueryCriteria criteria, @Param("pageable") Pageable pageable) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //分页
        Integer pageSize = criteria.getSize();
        Integer pageNumber = criteria.getPage()-1;
        Integer startIndex = pageNumber*pageSize;
        Integer endIndex = pageNumber*pageSize + pageSize;
        criteria.setSize(null);
        criteria.setPage(null);

        //设置默认查询条件
        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        Map<String,Object> resultMap = new HashMap<>();
        //user
        criteria.setDisposeObjType(WorkflowDisposeObjEnum.USER);
        criteria.setDisposeObjId(username);
        List<WorkbenchTodoDto> workbenchTodoDtoUserList = workbenchService.queryTodoList(criteria);
        //dept
        if(user.getIsContact()){
            criteria.setDisposeObjType(WorkflowDisposeObjEnum.DEPT);
            criteria.setDisposeObjId(user.getDept().getId().toString());
            List<WorkbenchTodoDto> workbenchTodoDtoDeptList = workbenchService.queryTodoList(criteria);
            workbenchTodoDtoUserList.addAll(workbenchTodoDtoDeptList);
        }
        //role
        Set<Role> roles = user.getRoles();
        for(Role role:roles){
            criteria.setDisposeObjType(WorkflowDisposeObjEnum.ROLE);
            criteria.setDisposeObjId(role.getId().toString());
            List<WorkbenchTodoDto> workbenchTodoDtoRoleList = workbenchService.queryTodoList(criteria);
            workbenchTodoDtoUserList.addAll(workbenchTodoDtoRoleList);
        }

        List<WorkbenchTodoDto> resultList = workbenchTodoDtoUserList.stream().sorted(Comparator.comparing(WorkbenchTodoDto::getCreateDate).reversed()).collect(Collectors.toList());

        //for (WorkbenchTodoDto workbenchTodoDto:workbenchTodoDtoList){
        //    workbenchTodoDto.set
        //}

        //过滤一些状态未完成，但是本人完成的任务
        WorkflowCommon workflowCommon = new WorkflowCommon();
        Iterator<WorkbenchTodoDto> iterator = resultList.iterator();
        //Map<String,Object> dictMap = new HashMap<>();
        //List<DictDetail> dictDetailList = dictDetailRepository.findByDictName("workflow_type");
        //dictDetailList.forEach(item->{
        //    dictMap.put(item.getValue(),item.getLabel());
        //});
        Map<String,String> typeMaping = new HashMap<>();
        typeMaping.put("system_abolition","abolition");
        typeMaping.put("system_approval","establishment");
        typeMaping.put("solicit_opinions","opinion");
        typeMaping.put("system_plan","plan");
        while (iterator.hasNext()){
            WorkbenchTodoDto workbenchTodoDto = iterator.next();
            workflowCommon.setId(workbenchTodoDto.getId());
            if (workflowLogRepository.countByWorkflowCommonAndStepLengthAndUsername(workflowCommon,workflowCommon.getStepLength(),username) != 0
                || feedbackOpinionRepository.countByWorkflowCommonIdAndStepSeqNumAndInitUser(workbenchTodoDto.getId(),workbenchTodoDto.getNowStepNum(),user) != 0){
                iterator.remove();
            }
            workbenchTodoDto.setWorkflowTypeName(workbenchTodoDto.getWorkflowType().getName());
            workbenchTodoDto.setBusinessType(typeMaping.get(workbenchTodoDto.getWorkflowType().getCode()));
        }
        Integer total = resultList.size();
        resultMap.put("totalElement",total);
        if( total > startIndex ){
            endIndex = total < endIndex ? total : endIndex;
            resultMap.put("content",resultList.subList(startIndex,endIndex));
        }else if(total < startIndex){
            resultMap.put("content",new ArrayList<>());
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Log("查询已办事项列表")
    @ApiOperation("查询已办事项列表")
    @PostMapping("/doneList")
    @PreAuthorize("@el.check('workbench:doneList')")
    public ResponseEntity<Object> queryDone(@RequestBody WorkbenchLogQueryCriteria queryCriteria, @Param("pageable") Pageable pageable) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //分页
        Integer pageSize = queryCriteria.getSize();
        Integer pageNumber = queryCriteria.getPage()-1;
        Integer startIndex = pageNumber*pageSize;
        Integer endIndex = pageNumber*pageSize + pageSize;
        queryCriteria.setSize(null);
        queryCriteria.setPage(null);

        //设置默认查询条件
        String username = SecurityUtils.getCurrentUsername();
        queryCriteria.setUsername(username);

        Map<String,Object> resultMap = new HashMap<>();
        //加载字典
        //List<DictDetail> dictDetailList = dictDetailRepository.findByDictName("workflow_type");
        //Map<String,Object> dictMap = new HashMap<>();
        //for (DictDetail dictDetail:dictDetailList){
        //    dictMap.put(dictDetail.getValue(),dictDetail.getLabel());
        //}
        List<WorkbenchDoneDto> resultList = new ArrayList<>();

        //排序并翻译流程类型
        Map<String,String> typeMaping = new HashMap<>();
        typeMaping.put("system_abolition","abolition");
        typeMaping.put("system_approval","establishment");
        typeMaping.put("solicit_opinions","opinion");
        typeMaping.put("system_plan","plan");
        List<WorkbenchDoneDto> workbenchDoneDtoList = workbenchService.queryDoneList(queryCriteria);
        if (!CollectionUtils.isEmpty(workbenchDoneDtoList)){
            resultList = workbenchDoneDtoList.stream().sorted(Comparator.comparing(WorkbenchDoneDto::getCreateDate).reversed()).collect(Collectors.toList());
            resultList.forEach(item->{
                item.setWorkflowTypeName(item.getWorkflowType().getName());
                item.setBusinessType(typeMaping.get(item.getWorkflowType().getCode()));
            });
        }
        Integer total = resultList.size();
        resultMap.put("totalElement",total);
        if( total > startIndex ){
            endIndex = total < endIndex ? total : endIndex;
            resultMap.put("content",resultList.subList(startIndex,endIndex));
        }else if(total < startIndex){
            resultMap.put("content",new ArrayList<>());
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    //@Log("添加意见流程")
    //@ApiOperation("添加意见流程")
    //@PostMapping("/addWorkflowOpinion")
    //@PreAuthorize("@el.check('workbench:addWorkflowOpinion')")
    //public ResponseEntity<Object> createOpinion(@Validated @RequestBody WorkflowOpinion resources){
    //    workbenchService.createOpinion(resources);
    //    return new ResponseEntity<>(HttpStatus.CREATED);
    //}


    @Log("更新流程信息")
    @ApiOperation("更新流程信息")
    @PutMapping("updateWorkflow")
    @PreAuthorize("@el.check('workbench:edit','opinion:edit'" +
            ",'establishment:edit','regulation:edit','systemDocument:edit')")
    public ResponseEntity<Object> update(@RequestBody WorkbenchVo workbenchVo) throws IOException {
        //取出需要校验字段
        String businessType = workbenchVo.getBusinessType();
        WorkflowLog workflowLog = workbenchVo.getWorkflowLog();
        Long commonId = workflowLog.getWorkflowCommon().getId();
        List<FeedbackOpinion> feedbackOpinionList = workbenchVo.getFeedbackOpinionList();
        if (StringUtils.isEmpty(businessType) || ObjectUtils.isEmpty(workflowLog)){
            throw new BadRequestException("必填字段不能为空！");
        }
        //logTestRepository.save(workflowLogTest);
        WorkflowCommon workflowCommon = workflowCommonRepository.findById(commonId).orElseGet(WorkflowCommon::new);
        if (!CollectionUtils.isEmpty(feedbackOpinionList)){
            feedbackOpinionRepository.saveAll(feedbackOpinionList);
        }
        switch (businessType){
            case "establishment":{
                if (workflowCommon.getNowStepNum() == 2){
                    workflowLog.setReminder(workflowCommon.getStartUsername().toString());
                }
                SystemDocument systemDocument  = workbenchVo.getSystemDocument();
                //establishmentService.uploadAttachments(systemDocument,workbenchVo.getAttachList());

                systemDocumentRepository.save(systemDocument);
                workbenchService.handleProcess(workflowCommon,workflowLog);
                break;
            }
            case "opinion":{
                Opinion opinion = workbenchVo.getOpinion();
                opinionRepository.save(opinion);
                workbenchService.handleProcess(workflowCommon,workflowLog);
                break;
            }
            case "plan":{
                //if (workflowCommon.getNowStepNum() == 2){
                //    workflowLog.setReminder(workflowCommon.getStartUsername().toString());
                //}
                Regulation regulation = workbenchVo.getRegulation();
                regulationRepository.save(regulation);
                workbenchService.handleProcess(workflowCommon,workflowLog);
                break;
                //break;
            }
            case "abolition":{
                if (workflowCommon.getNowStepNum() == 2){
                    workflowLog.setReminder(workflowCommon.getStartUsername().toString());
                }
                SystemDocument systemDocument  = workbenchVo.getSystemDocument();
                SystemDocumentFormVo systemDocumentFormVo = new SystemDocumentFormVo();
                systemDocumentFormVo.setFormObj(systemDocument);
                systemDocumentFormVo.setAttachList(workbenchVo.getAttachList());

                systemDocumentService.update(systemDocumentFormVo);
                //systemDocumentRepository.save(systemDocument);
                workbenchService.handleProcess(workflowCommon,workflowLog);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Log("批量更新计划流程信息")
    @ApiOperation("批量更新计划流程信息")
    @PutMapping("updateWorkflowSystemPlan")
    @PreAuthorize("@el.check('workbench:edit','opinion:edit'" +
            ",'establishment:edit','regulation:edit','systemDocument:edit')")
    public ResponseEntity<Object> updateListPlan(@RequestBody List<Long> commonIds) throws IOException {
        if (CollectionUtils.isEmpty(commonIds)){
            throw new BadRequestException("尚未选择要制度编修计划流程~");
       }
        List<WorkflowCommon> workflowCommonList = workflowCommonRepository.findAllById(commonIds);

        if (CollectionUtils.isEmpty(workflowCommonList)){
            throw new BadRequestException("所选制度编修计划查询为空~");
        }
        String username = SecurityUtils.getCurrentUsername();
        Dict dict = dictRepository.findByName("system_plan_step_name");
        Map<Integer,String> systemPlanStepNameMap = new HashMap<>();
        dict.getDictDetails().forEach(item->{
            systemPlanStepNameMap.put(Integer.valueOf(item.getLabel()),item.getValue());
        });
        workflowCommonList.forEach(item->{
            WorkflowLog workflowLog = new WorkflowLog();
            workflowLog.setUsername(username);
            workflowLog.setWorkflowCommon(new WorkflowCommon(item.getId()));
            workflowLog.setStepSeqNum(item.getNowStepNum());
            workflowLog.setStepName(systemPlanStepNameMap.get(item.getNowStepNum()));
            workflowLog.setStepLength(item.getStepLength());
            workflowLog.setAgreeFlag(true);
            workflowLog.setOpinion("同意");
            workbenchService.handleProcess(item,workflowLog);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //
    //@Log("查找下一步流程")
    //@ApiOperation("查找下一步流程")
    //@PostMapping("/nextStep")
    //@PreAuthorize("@el.check('workbench:nextStep')")
    //public ResponseEntity<Object> nextStep(@RequestBody Map<String,Object> paramMap) throws Exception {
    //    //workbenchService.create(resources);
    //    return new ResponseEntity<>(workflowDefineService.queryNextStep(paramMap), HttpStatus.OK);
    //}

    @Log("查询待办事项详情")
    @ApiOperation("查询待办事项详情")
    @PostMapping("/todoView")
    @PreAuthorize("@el.check('workbench:todoView')")
    public ResponseEntity<Object> todoView(@RequestBody WorkbenchQueryCriteria criteria)  {
        return new ResponseEntity<>(workbenchService.todoView(criteria), HttpStatus.OK);
    }
    @Log("查询审批记录")
    @ApiOperation("查询审批记录")
    @PostMapping("/doneView")
    @PreAuthorize("@el.check('workbench:doneView')")
    public ResponseEntity<Object> doneView(@RequestBody WorkbenchQueryCriteria criteria ) {
        //workbenchService.create(resources);
        return new ResponseEntity<>(workbenchService.doneView(criteria), HttpStatus.OK);
    }

    @Log("导出审批记录")
    @ApiOperation("导出审批记录")
    @PostMapping("/exportApprovalRecord")
    @PreAuthorize("@el.check('workbench:exportApprovalRecord')")
    public void exportApprovalRecord(@RequestBody Map<String,Object> param, HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> mp = new HashMap<>();
            //log.info(testDrive.toString());
            String fileName = new String();
            mp.put("exportData", getExcelList(param));
            List<DictDetail> dictDetailList = dictDetailRepository.findByDictName("template_name");
            for (DictDetail item : dictDetailList){
                if ("approval_record".equals(item.getValue())) {
                    fileName = item.getLabel();
                    break;
                }
            }
            ServletOutputStream out = response.getOutputStream();
            InputStream inputStream = WorkbenchController.class.getClassLoader()
                    .getResourceAsStream("public/excelTemplate/approvalRecordTemplate.xlsx");

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
            ExcelUtil util = new ExcelUtil();
            util.exportExcel(inputStream, mp,out);
            out.flush();
            out.close();

            //response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //response.setHeader("Content-Disposition", "attachment; filename=" + "审批记录" + ".xlsx");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Log("获取流程路径")
    @ApiOperation("获取流程路径")
    @PostMapping("/getProcessPath")
    @PreAuthorize("@el.check('workbench:getProcessPath')")
    public ResponseEntity<Object> getProcessPath(@RequestBody WorkbenchQueryCriteria criteria){
        Long id = criteria.getId();
        if (id == null){
            throw new BadRequestException("id 为空");
        }
        return new ResponseEntity<>(workbenchService.getProcessPath(id), HttpStatus.OK);
    }

    @Log("获取导出审批记录数据")
    @ApiOperation("获取导出审批记录数据")
    @PostMapping("/getExcelList")
    @PreAuthorize("@el.check('workbench:getExcelList')")
    public List getExcelList(@RequestBody Map<String,Object> param){
        Object commonIds = MapUtils.getObject(param,"ids");
        //存放查询log实体
        List<WorkflowLog> resultLogList = new ArrayList<>();
        //判空
        if(!ObjectUtils.isEmpty(commonIds)){
            //去重和构建查询参数
            List<WorkflowCommon> workflowCommonList = new ArrayList<>();
            List<Integer> commonIntegerIds = (List<Integer>) commonIds;
            Set<Long> ids = commonIntegerIds.stream().map(i->Long.valueOf(i.toString())).collect(Collectors.toSet());
            for(Long id : ids){
                WorkflowCommon workflowCommon = new WorkflowCommon();
                workflowCommon.setId(id);
                workflowCommonList.add(workflowCommon);
            }
            return workbenchService.getExcelList(workflowCommonList);
        }else {
            throw new BadRequestException("您尚无选择任何流程数据~");
        }
    }


}
