package com.zony.app.rest;

import com.zony.app.domain.Metadata;
import com.zony.app.service.MetadataService;
import com.zony.app.service.criteria.MetadataQueryCriteria;
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

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "元数据管理")
@RequestMapping("/api/metadata")
public class MetadataController {

    private final MetadataService metadataService;

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('metadata:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Metadata resources){
        metadataService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('metadata:edit')")
    public ResponseEntity<Object> update( @Validated (Metadata.Update.class) @RequestBody Metadata resources){
        metadataService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping
    @PreAuthorize("@el.check('metadata:delete')")
    public ResponseEntity<Object> delete( @RequestBody Set<Long> list ){
        if (CollectionUtils.isEmpty(list)){
            throw new BadRequestException("Set is empty! ");
        }
        metadataService.delete(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查询")
    @ApiOperation("查询")
    @GetMapping
    @PreAuthorize("@el.check('metadata:list')")
    public ResponseEntity<Object> query(MetadataQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(metadataService.queryAll(criteria,pageable),HttpStatus.OK);
    }
}
