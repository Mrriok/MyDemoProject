/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import com.zony.app.domain.Help;
//import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.vo.HelpDocumentFormVo;
import com.zony.app.service.HelpService;
import com.zony.app.service.criteria.HelpQueryCriteria;

import com.zony.common.annotation.Log;
import com.zony.common.annotation.rest.AnonymousPostMapping;
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
@Api(tags = "使用帮助")
@RequestMapping("/api/help")
public class HelpController {
    private final HelpService helpService;

    @Log("查询意见列表")
    @ApiOperation("查询意见列表")
    @GetMapping
    @PreAuthorize("@el.check('help:list')")
    public ResponseEntity<Object> query(HelpQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(helpService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @Log("新增帮助")
    @ApiOperation("新增帮助")
    @PostMapping
    @PreAuthorize("@el.check('help:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Help resources){
        helpService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("上传使用帮助")
    @ApiOperation("上传使用帮助")
    @AnonymousPostMapping
    @RequestMapping(value = "/importHelpExcel", method = RequestMethod.POST)
    @PreAuthorize("@el.check('help:importHelpExcel')")
    public ResponseEntity<Object> importHelpExcel(@Validated(Help.Create.class) @RequestBody HelpDocumentFormVo helpDocumentFormVo) {
        helpService.importHelpExcel(helpDocumentFormVo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("删除使用帮助")
    @ApiOperation("删除使用帮助")
    @DeleteMapping
    @PreAuthorize("@el.check('help:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            throw new BadRequestException("Set  is empty! ");
        }
        helpService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查询使用帮助详情")
    @ApiOperation("查询使用帮助详情")
    @GetMapping("/view")
    @PreAuthorize("@el.check('help:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id){
        return new ResponseEntity<>(helpService.view(id),HttpStatus.CREATED);
    }

}

