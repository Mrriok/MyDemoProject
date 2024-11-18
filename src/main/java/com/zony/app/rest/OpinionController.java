/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import com.zony.app.domain.vo.OpinionFormVo;
import com.zony.app.enums.ProgressStatusEnum;
import com.zony.app.repository.DeptRepository;
import com.zony.app.repository.DictDetailRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.OpinionService;
import com.zony.app.service.criteria.OpinionQueryCriteria;
import com.zony.app.service.criteria.SystemDocumentQueryCrietria;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.SecurityUtils;
import com.zony.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -16:13
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "征求意见")
@RequestMapping("/api/opinion")
public class OpinionController {
    private final OpinionService opinionService;
    private final DictDetailRepository dictDetailRepository;
    private final UserRepository userRepository;
    private final DeptRepository deptRepository;

    @Log("查询意见列表")
    @ApiOperation("查询意见列表")
    @GetMapping
    @PreAuthorize("@el.check('opinion:list')")
    public ResponseEntity<Object> query(OpinionQueryCriteria criteria, Pageable pageable){
        //dataFilterByDept(criteria);
        //jsonString
        criteria.setContactPersonIds("%," + SecurityUtils.getCurrentUsername() + ",%");
        return new ResponseEntity<>(opinionService.queryAll(criteria,pageable),HttpStatus.OK);
    }
    @Log("查询意见详情")
    @ApiOperation("查询意见详情")
    @GetMapping("/view")
    @PreAuthorize("@el.check('opinion:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id){
        return new ResponseEntity<>(opinionService.findById(id),HttpStatus.OK);
    }
    @Log("新增意见")
    @ApiOperation("新增意见")
    @PostMapping
    @PreAuthorize("@el.check('opinion:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody OpinionFormVo opinionFormVo){
        opinionService.create(opinionFormVo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("修改意见记录")
    @ApiOperation("修改意见记录")
    @PutMapping
    @PreAuthorize("@el.check('opinion:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody OpinionFormVo resources){
        opinionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除意见记录")
    @ApiOperation("删除意见记录")
    @DeleteMapping
    @PreAuthorize("@el.check('opinion:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            throw new BadRequestException("Set  is empty! ");
        }
        opinionService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("修改征求意见状态")
    @ApiOperation("修改征求意见状态")
    @PutMapping(value = "/operating")
    @PreAuthorize("@el.check('propagation:operating')")
    public ResponseEntity<Object> operating(@RequestBody @NotNull Map<String,Object> paramMap){
        Long id = MapUtils.getLong(paramMap,"id");
        String status = MapUtils.getString(paramMap,"status");
        opinionService.operating(id,ProgressStatusEnum.getItem(status));
        //待补充站内提醒包括邮件提醒
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //private void dataFilterByDept(OpinionQueryCriteria criteria) {
    //    Long deptId = userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername());
    //    String riskControl = dictDetailRepository.findByDictNameAndLabel("not_filter_data_dept","risk_control").getValue();
    //    List<Long> riskControlIds = new ArrayList<>();
    //    if (!StringUtils.isEmpty(riskControl)){
    //        List<String> riskControlIntegerIds = Arrays.asList(riskControl.split(","));
    //        riskControlIds = riskControlIntegerIds.stream().map(i->Long.valueOf(i)).collect(Collectors.toList());
    //    }
    //    if (!riskControlIds.contains(deptId)){
    //        criteria.setDeptId(userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername()));
    //    }//风控部门包含当前deptId需要处理
    //}
}
