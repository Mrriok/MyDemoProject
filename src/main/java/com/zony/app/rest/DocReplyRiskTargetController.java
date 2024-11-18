/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import com.zony.app.domain.DocReplyRiskTarget;
import com.zony.app.service.DocReplyRiskTargetService;
import com.zony.app.service.criteria.DocReplyRiskTargetQueryCriteria;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -16:13
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "制度编制：主要面对风险及目标")
@RequestMapping("/api/docReplyRiskTarget")
public class DocReplyRiskTargetController {
    private final DocReplyRiskTargetService docReplyRiskTargetService;

    @Log("查询风险列表")
    @ApiOperation("查询风险列表")
    @GetMapping
    @PreAuthorize("@el.check('docReplyRiskTarget:list')")
    public ResponseEntity<Object> query(DocReplyRiskTargetQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(docReplyRiskTargetService.queryAll(criteria,pageable),HttpStatus.OK);
    }
    @Log("查询风险详情")
    @ApiOperation("查询意见详情")
    @GetMapping("/view")
    @PreAuthorize("@el.check('docReplyRiskTarget:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id){
        return new ResponseEntity<>(docReplyRiskTargetService.view(id),HttpStatus.OK);
    }
    @Log("新增风险")
    @ApiOperation("新增风险")
    @PostMapping
    @PreAuthorize("@el.check('docReplyRiskTarget:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DocReplyRiskTarget docReplyRiskTarget){
        docReplyRiskTargetService.create(docReplyRiskTarget);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("修改风险记录")
    @ApiOperation("修改风险记录")
    @PutMapping
    @PreAuthorize("@el.check('docReplyRiskTarget:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DocReplyRiskTarget resources){
        docReplyRiskTargetService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除风险记录")
    @ApiOperation("删除风险记录")
    @DeleteMapping
    @PreAuthorize("@el.check('docReplyRiskTarget:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            throw new BadRequestException("Set  is empty! ");
        }
        docReplyRiskTargetService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
