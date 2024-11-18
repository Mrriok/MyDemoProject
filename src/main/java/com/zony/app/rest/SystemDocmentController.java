package com.zony.app.rest;

import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.vo.SystemDocumentFormVo;
import com.zony.app.service.SystemDocumentService;
import com.zony.app.service.criteria.SystemDocumentQueryCrietria;
import com.zony.app.service.mapstruct.SystemDocumentMapper;
import com.zony.common.annotation.Log;
import com.zony.common.annotation.rest.AnonymousDeleteMapping;
import com.zony.common.annotation.rest.AnonymousPostMapping;
import com.zony.common.annotation.rest.AnonymousPutMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

@Api(tags = "制度文档管理")
@RestController
@RequestMapping("/api/system_document")
@RequiredArgsConstructor
@EnableAsync
public class SystemDocmentController {

    private final SystemDocumentService systemDocumentService;
    private final SystemDocumentMapper systemDocumentMapper;

    @Log("新增制度信息")
    @ApiOperation("新增制度信息")
    @AnonymousPostMapping
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @PreAuthorize("@el.check('system_document:submit')")
    public ResponseEntity<Object> submit(@Validated(SystemDocument.Create.class) @RequestBody SystemDocumentFormVo submitVo) {
        submitVo.setFormObj(systemDocumentMapper.toEntity(submitVo.getFormDtoObj()));
        systemDocumentService.create(submitVo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("查询制度详情")
    @ApiOperation("查询制度详情")
    @AnonymousPostMapping
    @GetMapping("/view")
    @PreAuthorize("@el.check('system_document:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id) {
        return new ResponseEntity<>(systemDocumentService.view(id), HttpStatus.OK);
    }

    @Log("修改制度信息")
    @ApiOperation("修改制度信息")
    @AnonymousPutMapping
    @PutMapping("/update")
    @PreAuthorize("@el.check('system_document:edit')")
    public ResponseEntity<Object> update(@Validated(SystemDocument.Update.class) @RequestBody SystemDocumentFormVo submitVo) {
        submitVo.setFormObj(systemDocumentMapper.toEntity(submitVo.getFormDtoObj()));
        systemDocumentService.update(submitVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查询制度信息")
    @ApiOperation("查询计划制度信息")
    @GetMapping
    @PreAuthorize("@el.check('system_document:list')")
    public ResponseEntity<Object> query(SystemDocumentQueryCrietria criteria, Pageable pageable){
        return new ResponseEntity<>(systemDocumentService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("多选删除制度信息")
    @ApiOperation("多选删除制度信息")
    @AnonymousDeleteMapping
    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        systemDocumentService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
