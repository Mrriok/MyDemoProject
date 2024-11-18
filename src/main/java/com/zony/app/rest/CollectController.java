package com.zony.app.rest;

import com.zony.app.domain.Collect;
import com.zony.app.domain.SystemDocument;
import com.zony.app.service.CollectService;
import com.zony.app.service.criteria.CollectQueryCriteria;
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

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "制度收藏")
@RequestMapping("/api/collect")
public class CollectController {

    private final CollectService collectService;

    @Log("取消收藏")
    @ApiOperation("取消收藏")
    @DeleteMapping
    @PreAuthorize("@el.check('collect:delete')")
    public ResponseEntity<Object> delete( @RequestBody Set<Long> list ){
        if (CollectionUtils.isEmpty(list)){
            throw new BadRequestException("Set is empty! ");
        }
        collectService.delete(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查询收藏")
    @ApiOperation("查询收藏")
    @GetMapping
    @PreAuthorize("@el.check('collect:list')")
    public ResponseEntity<Object> query(CollectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(collectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增收藏")
    @ApiOperation("新增收藏")
    @PostMapping
    @PreAuthorize("@el.check('collect:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Set<Long> systemDocumentIds){
        if (CollectionUtils.isEmpty(systemDocumentIds)){
            throw new BadRequestException("Set is empty! ");
        }
        collectService.create(systemDocumentIds);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("收藏详情")
    @ApiOperation("收藏详情")
    @GetMapping("/view")
    @PreAuthorize("@el.check('collect:view')")
    public ResponseEntity<Object> view(@RequestParam Long id){
        return new ResponseEntity<>(collectService.view(id),HttpStatus.OK);
    }
}
