package com.zony.app.rest;

import com.zony.app.domain.Regulationsort;
import com.zony.app.service.RegulationsortService;
import com.zony.app.service.dto.RegulationsortDto;
import com.zony.app.service.criteria.RegulationsortQueryCriteria;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "制度分类")
@RequestMapping("/api/regulationsort")
public class RegulationsortController {

    private final RegulationsortService regulationsortService;

    @Log("新增制度分类")
    @ApiOperation("新增制度分类")
    @PostMapping
    @PreAuthorize("@el.check('regulationsort:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Regulationsort resources){
        regulationsortService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Log("查询制度分类")
    @ApiOperation("查询制度分类")
    @GetMapping
    @PreAuthorize("@el.check('regulationsort:list')")
    public ResponseEntity<Object> query(RegulationsortQueryCriteria criteria) throws Exception{
        List<RegulationsortDto> regulationsortDtos = regulationsortService.queryAll(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(regulationsortDtos, regulationsortDtos.size()),HttpStatus.OK);
    }

    @Log("查询制度分类")
    @ApiOperation("查询制度分类:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('user:list','regulationsort:list')")
    public ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids) {
        Set<RegulationsortDto> regulationsortDtos  = new LinkedHashSet<>();
        for (Long id : ids) {
            RegulationsortDto regulationsortDto = regulationsortService.findById(id);
            List<RegulationsortDto> regulationsorts = regulationsortService.getSuperior(regulationsortDto, new ArrayList<>());
            regulationsortDtos.addAll(regulationsorts);
        }
        return new ResponseEntity<>(regulationsortService.buildTree(new ArrayList<>(regulationsortDtos)),HttpStatus.OK);
    }

    @Log("修改制度分类")
    @ApiOperation("修改制度分类")
    @PutMapping
    @PreAuthorize("@el.check('regulationsort:edit')")
    public ResponseEntity<Object> update(@Validated(Regulationsort.Update.class) @RequestBody Regulationsort resources){
        regulationsortService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除制度分类")
    @ApiOperation("删除制度分类")
    @DeleteMapping
    @PreAuthorize("@el.check('regulationsirt:delete')")
    public ResponseEntity<Object> delete( @RequestBody Set<Long> list ){
        if (CollectionUtils.isEmpty(list)){
            throw new BadRequestException("Set is empty! ");
        }
        regulationsortService.delete(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
