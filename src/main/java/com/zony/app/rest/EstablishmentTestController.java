///**
// * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
// * Use is subject to license terms.<br>
// * <p>
// * 在此处填写文件说明
// */
//package com.zony.app.rest;
//
//import com.zony.app.domain.SystemDocument;
//import com.zony.app.enums.InstStatusEnum;
//import com.zony.app.service.EstablishmentServiceTest;
//import com.zony.app.service.criteria.SystemDocumentQueryCrietria;
//import com.zony.common.annotation.Log;
//import com.zony.common.exception.BadRequestException;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.collections4.MapUtils;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.util.CollectionUtils;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.constraints.NotNull;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 填写功能简述.
// * <p>填写详细说明.<br>
// * @version v1.0
// * @author MrriokChen
// * @date 2020/9/9 -16:17
// */
//@RestController
//@RequiredArgsConstructor
//@Api(tags = "制度编制")
//@RequestMapping("/api/establishmentTest")
//public class EstablishmentTestController {
//
//    private final EstablishmentServiceTest establishmentServiceTest;
//    //private final EstablishmentService establishmentService;
//    @Log("查询制度编制")
//    @ApiOperation("查询制度编制列表")
//    @GetMapping
//    @PreAuthorize("@el.check('establishment:list')")
//    public ResponseEntity<Object> query(SystemDocumentQueryCrietria criteria, Pageable pageable){
//        return new ResponseEntity<>(establishmentServiceTest.queryAll(criteria,pageable), HttpStatus.OK);
//    }
//
//    @Log("查询制度编制详情")
//    @ApiOperation("查询制度编制详情")
//    @GetMapping("/view")
//    @PreAuthorize("@el.check('establishment:view')")
//    public ResponseEntity<Object> view(@RequestParam @NotNull Long id){
//        return new ResponseEntity<>(establishmentServiceTest.findById(id),HttpStatus.OK);
//    }
//    @Log("新增制度编制")
//    @ApiOperation("新增制度编制")
//    @PostMapping("/add")
//    @PreAuthorize("@el.check('establishment:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody SystemDocument resources){
//        establishmentServiceTest.create(resources);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//    @Log("修改制度编制")
//    @ApiOperation("修改制度编制")
//    @PutMapping
//    @PreAuthorize("@el.check('establishment:edit')")
//    public ResponseEntity<Object> update(@Validated(SystemDocument.Update.class) @RequestBody SystemDocument resources){
//        establishmentServiceTest.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @Log("删除制度编制")
//    @ApiOperation("删除制度编制")
//    @DeleteMapping
//    @PreAuthorize("@el.check('establishment:del')")
//    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
//        if (CollectionUtils.isEmpty(ids)){
//            throw new BadRequestException("Set  is empty! ");
//        }
//        establishmentServiceTest.delete(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @Log("修改制度编制状态")
//    @ApiOperation("修改制度编制状态")
//    @PutMapping(value = "/operating")
//    @PreAuthorize("@el.check('propagation:operating')")
//    public ResponseEntity<Object> operating(@RequestBody @NotNull Map<String,Object> paramMap){
//        Long id = MapUtils.getLong(paramMap,"id");
//        String status = MapUtils.getString(paramMap,"status");
//        establishmentServiceTest.operating(id, InstStatusEnum.getItem(status));
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//}
