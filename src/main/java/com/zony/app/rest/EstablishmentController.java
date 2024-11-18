/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.vo.SystemDocumentFormVo;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.repository.DeptRepository;
import com.zony.app.repository.DictDetailRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.EditService;
import com.zony.app.service.EstablishmentService;
import com.zony.app.service.criteria.RegulationQueryCriteria;
import com.zony.app.service.criteria.SystemDocumentQueryCrietria;
import com.zony.app.service.dto.SystemDocumentDto;
import com.zony.app.service.dto.SystemDocumentWordDto;
import com.zony.app.service.mapstruct.SystemDocumentMapper;
import com.zony.app.utils.DateUtils;
import com.zony.common.annotation.Log;
import com.zony.common.annotation.rest.AnonymousGetMapping;
import com.zony.common.annotation.rest.AnonymousPostMapping;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.SecurityUtils;
import com.zony.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 *
 * @author MrriokChen
 * @version v1.0
 * @date 2020/7/30 -15:43
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "制度编制")
@RequestMapping("/api/establishment")
public class EstablishmentController {

    private final EstablishmentService establishmentService;
    private final EditService editService;
    private final SystemDocumentMapper systemDocumentMapper;
    private final DictDetailRepository dictDetailRepository;
    private final UserRepository userRepository;
    private final DeptRepository deptRepository;

    private static final Logger logger = LoggerFactory.getLogger(EstablishmentController.class);

    @Log("查询制度编制")
    @ApiOperation("查询制度编制列表")
    @GetMapping
    @PreAuthorize("@el.check('establishment:list')")
    public ResponseEntity<Object> query(SystemDocumentQueryCrietria criteria, Pageable pageable) {
        dataFilterByDept(criteria);
        return new ResponseEntity<>(establishmentService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("查询制度编制详情")
    @ApiOperation("查询制度编制详情")
    @GetMapping("/view")
    @PreAuthorize("@el.check('establishment:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id) {
        return new ResponseEntity<>(establishmentService.findById(id), HttpStatus.OK);
    }

    @Log("新增制度编制")
    @ApiOperation("新增制度编制")
    @PostMapping("/add")
    @PreAuthorize("@el.check('establishment:add')")
    public ResponseEntity<Object> create(@RequestBody SystemDocumentFormVo systemDocumentFormVo) throws IOException {
        if (ObjectUtils.isEmpty(systemDocumentFormVo.getFormDtoObj())) {
            throw new BadRequestException("表单数据为空！");
        }
        systemDocumentFormVo.setFormObj(systemDocumentMapper.toEntity(systemDocumentFormVo.getFormDtoObj()));
        establishmentService.create(systemDocumentFormVo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //@Log("上传附件")
    //@ApiOperation("上传附件")
    //@PostMapping(value = "/uploadAttachments")
    //public ResponseEntity<Object> uploadAttachments(@RequestBody SystemDocumentFormVo resources) throws IOException {
    //
    //    if (CollectionUtils.isEmpty(resources.getAttachList())
    //            || StringUtils.isEmpty(resources.getFormObj().getSystemTitle())
    //            || StringUtils.isEmpty(resources.getFormObj().getSystemCode())) {
    //        throw new BadRequestException("附件列表为空！");
    //    }
    //    SystemDocumentDto systemDocumentDto = resources.getFormObj();
    //    SystemDocument systemDocument = systemDocumentMapper.toEntity(systemDocumentDto);
    //    establishmentService.uploadAttachments(systemDocument,resources.getAttachList());
    //    return new ResponseEntity<>(HttpStatus.OK);
    //}

    @Log("浏览附件列表")
    @ApiOperation("浏览附件列表")
    @GetMapping(value = "/viewAttachments")
    public ResponseEntity<Object> viewAttachments(@RequestParam @NotNull Long id) throws IOException {
        return new ResponseEntity<>(establishmentService.viewAttachments(id), HttpStatus.OK);
    }

    @Log("修改制度编制")
    @ApiOperation("修改制度编制")
    @PutMapping
    @PreAuthorize("@el.check('establishment:edit')")
    public ResponseEntity<Object> update(@Validated(SystemDocument.Update.class) @RequestBody SystemDocumentFormVo systemDocumentFormVo) throws IOException {
        systemDocumentFormVo.setFormObj(systemDocumentMapper.toEntity(systemDocumentFormVo.getFormDtoObj()));
        establishmentService.update(systemDocumentFormVo);
        //uploadAttachments(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除制度编制")
    @ApiOperation("删除制度编制")
    @DeleteMapping
    @PreAuthorize("@el.check('establishment:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BadRequestException("Set  is empty! ");
        }
        establishmentService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("修改制度编制状态")
    @ApiOperation("修改制度编制状态")
    @PutMapping(value = "/operating")
    @PreAuthorize("@el.check('establishment:operating')")
    public ResponseEntity<Object> operating(@RequestBody @NotNull Map<String, Object> paramMap) {
        Long id = MapUtils.getLong(paramMap, "id");
        String status = MapUtils.getString(paramMap, "instStatus");
        establishmentService.operating(id, InstStatusEnum.getItem(status));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //
    //@Log("修改制度编制状态测试")
    //@ApiOperation("修改制度编制状态测试")
    //@PutMapping(value = "/operatingTest")
    //@PreAuthorize("@el.check('propagation:operatingTest')")
    //public ResponseEntity<Object> operatingTest(@RequestBody @NotNull Map<String,Object> paramMap){
    //    Long id = MapUtils.getLong(paramMap,"id");
    //    String status = MapUtils.getString(paramMap,"status");
    //    establishmentService.operatingTest(id, InstStatusEnum.getItem(status));
    //    //待补充站内提醒包括邮件提醒
    //    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //}

    /**
     * 读取制度正文
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    @Log("根据模版和元数据生成制度正文")
    @ApiOperation("根据模版和元数据生成制度正文")
    @AnonymousPostMapping(value = "/createText")
    @PreAuthorize("@el.check('establishment:createText')")
    public ResponseEntity<Object> createText(@RequestBody SystemDocumentFormVo systemDocumentFormVo) throws Exception {
        systemDocumentFormVo.setFormObj(systemDocumentMapper.toEntity(systemDocumentFormVo.getFormDtoObj()));
        //try {
        //
        //}catch (Exception e){
        //    logger.debug(e.toString());
        //    throw new BadRequestException("制度正文格式错误，解析失败~");
        //}
        return new ResponseEntity<>(editService.writeToWord(systemDocumentFormVo.getFormObj()), HttpStatus.OK);
    }

    /**
     * 构造制度正文
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    @Log("从制度正文导出元数据")
    @ApiOperation("从制度正文导出元数据")
    @AnonymousGetMapping(value = "/importText")
    @PreAuthorize("@el.check('establishment:importText')")
    public ResponseEntity<Object> importText(String fileName) {
        SystemDocument systemDocument = editService.readFromWord(fileName);
        return new ResponseEntity<>(systemDocumentMapper.toDto(systemDocument), HttpStatus.OK);
    }

    private void dataFilterByDept(SystemDocumentQueryCrietria criteria) {
        Long deptId = userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername());
        String riskControl = dictDetailRepository.findByDictNameAndLabel("not_filter_data_dept","risk_control").getValue();
        List<Long> riskControlIds = new ArrayList<>();
        if (!StringUtils.isEmpty(riskControl)){
            List<String> riskControlIntegerIds = Arrays.asList(riskControl.split(","));
            riskControlIds = riskControlIntegerIds.stream().map(i->Long.valueOf(i)).collect(Collectors.toList());
        }
        if (!riskControlIds.contains(deptId)){
            criteria.setDeptId(userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername()));
        }//风控部门包含当前deptId需要处理
    }
}
