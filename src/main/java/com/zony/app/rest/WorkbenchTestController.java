///**
// * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
// * Use is subject to license terms.<br>
// * <p>
// * 在此处填写文件说明
// */
//package com.zony.app.rest;
//
//import com.zony.app.domain.*;
//import com.zony.app.domain.vo.WorkbenchTestVo;
//import com.zony.app.enums.WorkflowDisposeObjEnum;
//import com.zony.app.repository.*;
//import com.zony.app.service.criteria.WorkbenchLogQueryCriteria;
//import com.zony.app.service.criteria.WorkbenchQueryCriteria;
//import com.zony.app.service.dto.WorkbenchTodoTestDto;
//import com.zony.common.annotation.Log;
//import com.zony.common.exception.BadRequestException;
//import com.zony.common.exception.EntityNotFoundException;
//import com.zony.common.utils.SecurityUtils;
//import com.zony.common.utils.StringUtils;
//import io.lettuce.core.dynamic.annotation.Param;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * 填写功能简述.
// * <p>填写详细说明.<br>
// * @version v1.0
// * @author MrriokChen
// * @date 2020/8/11 -16:16
// */
//@RestController
//@RequiredArgsConstructor
//@Api(tags = "工作台")
//@RequestMapping("/api/workbenchTest")
//public class WorkbenchTestController {
//    private final WorkflowMasterRepository workflowMasterRepository;
//    private final WorkflowLogTestRepository logTestRepository;
//    private final UserRepository userRepository;
//    private final WorkflowFeedbackRepository feedbackOpinionRepository;
//    private final EstablishmentRepository establishmentRepository;
//    private final OpinionRepository opinionRepository;
//
//    @Log("查询待办事项列表")
//    @ApiOperation("查询待办事项列表")
//    @PostMapping("/todoList")
//    @PreAuthorize("@el.check('workbench:todoList')")
//    public ResponseEntity<Object> queryTodo(@RequestBody WorkbenchQueryCriteria criteria, @Param("pageable") Pageable pageable) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//
//        return null;
//    }
//
//    @Log("查询已办事项列表")
//    @ApiOperation("查询已办事项列表")
//    @PostMapping("/doneList")
//    @PreAuthorize("@el.check('workbench:doneList')")
//    public ResponseEntity<Object> queryDone(@RequestBody WorkbenchLogQueryCriteria queryCriteria, @Param("pageable") Pageable pageable) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
//
//    @Log("更新流程信息")
//    @ApiOperation("更新流程信息")
//    @PutMapping("updateWorkflow")
//    @PreAuthorize("@el.check('workbench:update')")
//    public ResponseEntity<Object> update(@RequestBody WorkbenchTestVo workbenchTestVo){
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @Log("查找下一步流程")
//    @ApiOperation("查找下一步流程")
//    @PostMapping("/nextStep")
//    @PreAuthorize("@el.check('workbench:nextStep')")
//    public ResponseEntity<Object> nextStep(@RequestBody Map<String,Object> paramMap) throws Exception {
//        return null;
//    }
//    @Log("查询待办事项详情")
//    @ApiOperation("查询待办事项详情")
//    @PostMapping("/todoView")
//    @PreAuthorize("@el.check('workbench:todoView')")
//    public ResponseEntity<Object> todoView(@RequestBody WorkbenchQueryCriteria criteria)  {
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//    @Log("查询审批记录")
//    @ApiOperation("查询审批记录")
//    @PostMapping("/doneView")
//    @PreAuthorize("@el.check('workbench:doneView')")
//    public ResponseEntity<Object> doneView(@RequestBody WorkbenchQueryCriteria criteria ) {
//       return null;
//    }
//}
