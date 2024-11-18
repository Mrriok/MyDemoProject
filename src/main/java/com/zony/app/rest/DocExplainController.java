/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import com.zony.app.domain.DocExplain;
import com.zony.app.service.DocExplainService;
import com.zony.app.service.criteria.DocExplainQueryCriteria;
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
@Api(tags = "制度编制：释义")
@RequestMapping("/api/docExplain")
public class DocExplainController {
    private final DocExplainService docExplainService;

    @Log("查询释义列表")
    @ApiOperation("查询释义列表")
    @GetMapping
    @PreAuthorize("@el.check('docExplain:list')")
    public ResponseEntity<Object> query(DocExplainQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(docExplainService.queryAll(criteria,pageable),HttpStatus.OK);
    }
    @Log("查询释义详情")
    @ApiOperation("查询释义详情")
    @GetMapping("/view")
    @PreAuthorize("@el.check('docExplain:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id){
        return new ResponseEntity<>(docExplainService.view(id),HttpStatus.OK);
    }
    @Log("新增释义")
    @ApiOperation("新增释义")
    @PostMapping
    @PreAuthorize("@el.check('docExplain:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DocExplain docExplain){
        docExplainService.create(docExplain);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("修改释义记录")
    @ApiOperation("修改释义记录")
    @PutMapping
    @PreAuthorize("@el.check('docExplain:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DocExplain docExplain){
        docExplainService.update(docExplain);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除释义记录")
    @ApiOperation("删除释义记录")
    @DeleteMapping
    @PreAuthorize("@el.check('docExplain:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            throw new BadRequestException("Set  is empty! ");
        }
        docExplainService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
