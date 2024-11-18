package com.zony.app.rest;

import com.google.common.collect.ImmutableMap;
import com.zony.app.service.RegulationlibraryService;
import com.zony.app.service.criteria.RegulationlibraryQueryCriteria;
import com.zony.common.annotation.Log;
import com.zony.common.annotation.rest.AnonymousGetMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "制度库管理")
@RequestMapping("/api/regulationlibrary")
public class RegulationlibraryController {

    private final RegulationlibraryService regulationlibraryService;

    @Log("查询制度")
    @ApiOperation("查询制度")
    @AnonymousGetMapping("/query")
    public ResponseEntity<Object> query(RegulationlibraryQueryCriteria criteria) {
        return new ResponseEntity<>(ImmutableMap.of("content", regulationlibraryService.queryMultiLevelList(criteria)), HttpStatus.OK);
    }

    @Log("查看制度层级图形")
    @ApiOperation("查看制度层级图形")
    @AnonymousGetMapping("/queryLevelImage")
    public ResponseEntity<Object> queryLevelImage(String systemCode) {
        return new ResponseEntity<>(regulationlibraryService.queryLevelImage(systemCode), HttpStatus.OK);
    }

    @Log("查看制度详情")
    @ApiOperation("查看制度详情")
    @AnonymousGetMapping("/view")
    public ResponseEntity<Object> view(String systemCode) {
        return new ResponseEntity<>(regulationlibraryService.view(systemCode), HttpStatus.OK);
    }

    @Log("废除计划")
    @ApiOperation("废除计划")
    @PutMapping("/abolition")
    @PreAuthorize("@el.check('SystemDocment:abolition')")
    public ResponseEntity<Object> abolition(@RequestBody RegulationlibraryQueryCriteria criteria){
        //废除计划，取id操作
        regulationlibraryService.abolition(criteria);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    @todo  打包下载未完成
    @date  2020-10-10
    **/

    @Log("制度文档打包下载")
    @ApiOperation("制度文档打包下载")
    @PutMapping("/downloadPackage")
    @PreAuthorize("@el.check('SystemDocment:downloadPackage')")
    public ResponseEntity<Object> downloadPackage(@RequestBody RegulationlibraryQueryCriteria criteria){
        //废除计划，取id操作
        regulationlibraryService.abolition(criteria);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
