package com.zony.app.rest;

import com.zony.app.domain.Baselibrary;
import com.zony.app.domain.vo.AttachFormVo;
import com.zony.app.service.BaselibraryService;
import com.zony.app.service.criteria.BaselibraryQueryCriteria;
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
import java.io.IOException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "编制依据库")
@RequestMapping("/api/baselibrary")
public class BaselibraryController {

    private final BaselibraryService baseLibraryService;

    @Log("删除引用文档")
    @ApiOperation("删除引用文档")
    @DeleteMapping
    @PreAuthorize("@el.check('baselibrary:delete')")
    public ResponseEntity<Object> delete( @RequestBody Set<Long> list ){
        if (CollectionUtils.isEmpty(list)){
            throw new BadRequestException("Set is empty! ");
        }
        baseLibraryService.delete(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("上传")
    @ApiOperation("上传")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('baselibrary:add','baselibrary:update')")
    public ResponseEntity<Object> upload(@RequestBody AttachFormVo resources) throws IOException {
        if (CollectionUtils.isEmpty(resources.getAttachList())){
            throw new BadRequestException("附件列表为空！");
        }
        baseLibraryService.upload(resources.getAttachList());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查询依据库文档")
    @ApiOperation("查询依据库文档")
    @GetMapping
    @PreAuthorize("@el.check('baselibrary:list')")
    public ResponseEntity<Object> query(BaselibraryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(baseLibraryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增文件")
    @ApiOperation("新增文档")
    @PostMapping
    @PreAuthorize("@el.check('baselibrary:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Baselibrary resources){
        baseLibraryService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改文件")
    @ApiOperation("修改文件")
    @PutMapping
    @PreAuthorize("@el.check('baselibrary:update')")
    public ResponseEntity<Object> update(@Validated @RequestBody Baselibrary resources){
        baseLibraryService.update(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("查询详情")
    @ApiOperation("查询详情")
    @GetMapping("/view")
    @PreAuthorize("@el.check('baselibrary:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id){
        return new ResponseEntity<>(baseLibraryService.view(id),HttpStatus.CREATED);
    }
}
