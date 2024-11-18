package com.zony.app.rest;

import com.zony.app.domain.Jurisdiction;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.vo.JurisdictionDocumentFormVo;
import com.zony.app.service.JurisdictionService;
import com.zony.app.service.criteria.JurisdictionQueryCriteria;
import com.zony.common.annotation.Log;
import com.zony.common.annotation.rest.AnonymousPostMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "权限手册")
@RequestMapping("/api/jurisdiction")
public class JurisdictionController {

    private final JurisdictionService jurisdictionServiceBak;

    @Log("新增权限手册")
    @ApiOperation("新增权限手册")
    @PostMapping
    @PreAuthorize("@el.check('jurisdiction:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Jurisdiction resources){
        jurisdictionServiceBak.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Log("查询依据库文档")
    @ApiOperation("查询依据库文档")
    @GetMapping
    @PreAuthorize("@el.check('jurisdiction:list')")
    public ResponseEntity<Object> query(JurisdictionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(jurisdictionServiceBak.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("上传权限手册")
    @ApiOperation("上传权限手册")
    @AnonymousPostMapping
    @RequestMapping(value = "/importJurisdictionExcel", method = RequestMethod.POST)
    @PreAuthorize("@el.check('jurisdiction:importJurisdictionExcel')")
    public ResponseEntity<Object> importJurisdictionExcel(@Validated(SystemDocument.Create.class) @RequestBody JurisdictionDocumentFormVo jurisdictionDocumentFormVo) {
        jurisdictionServiceBak.importJurisdictionExcel(jurisdictionDocumentFormVo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("删除权限手册")
    @ApiOperation("删除权限手册")
    @DeleteMapping
    @PreAuthorize("@el.check('jurisdiction:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        jurisdictionServiceBak.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
