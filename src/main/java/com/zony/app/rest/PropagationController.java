
package com.zony.app.rest;

import com.zony.app.domain.Propagation;
import com.zony.app.domain.vo.PropagationFormVo;
import com.zony.app.service.criteria.PropagationQueryCriteria;
import com.zony.app.service.*;
import com.zony.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@Api(tags = "制度宣贯管理")
@RestController
@RequestMapping("/api/propagations")
@RequiredArgsConstructor
public class PropagationController {

    private final PropagationService propagationService;
    private final UserService userService;
    private final DataService dataService;


    @Log("查询制度宣贯列表")
    @ApiOperation("查询制度宣贯")
    @GetMapping
    @PreAuthorize("@el.check('propagation:list')")
    public ResponseEntity<Object> query(PropagationQueryCriteria criteria, Pageable pageable){
            return new ResponseEntity<>(propagationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增制度宣贯记录")
    @ApiOperation("新增制度宣贯记录")
    @PostMapping
    @PreAuthorize("@el.check('propagation:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PropagationFormVo propagationFormVo) throws IOException {
        if (ObjectUtils.isEmpty(propagationFormVo.getFormObj())) {
            throw new BadRequestException("表单数据为空！");
        }
        propagationService.create(propagationFormVo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("修改制度宣贯记录")
    @ApiOperation("修改制度宣贯记录")
    @PutMapping
    @PreAuthorize("@el.check('propagation:edit')")
    public ResponseEntity<Object> update(@Validated(Propagation.Update.class) @RequestBody PropagationFormVo propagationFormVo) throws IOException {
        //if(!SecurityUtils.getCurrentUsername().equals(resources.getUsername())){
        //    throw new BadRequestException("当前用户无权限修改本条记录");
        //}
        propagationService.update(propagationFormVo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除制度宣贯记录")
    @ApiOperation("删除制度宣贯记录")
    @DeleteMapping
    @PreAuthorize("@el.check('propagation:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        if (CollectionUtils.isEmpty(ids)) {
            throw new BadRequestException("Set  is empty! ");
        }
        propagationService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查看制度宣贯")
    @ApiOperation("查看制度宣贯")
    @GetMapping(value = "/view")
    @PreAuthorize("@el.check('propagation:view')")
    public ResponseEntity<Object> view(@RequestParam Long id){

        return new ResponseEntity<>(propagationService.findById(id),HttpStatus.OK);
    }

    @Log("发起制度宣贯")
    @ApiOperation("修改制度宣贯")
    @PutMapping(value = "/launch")
    @PreAuthorize("@el.check('propagation:launch')")
    public ResponseEntity<Object> launch(@RequestBody Map<String,Object> param){

        propagationService.launch(MapUtils.getLong(param,"id"));
        //待补充站内提醒包括邮件提醒
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
