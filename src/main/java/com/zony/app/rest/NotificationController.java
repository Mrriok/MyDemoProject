/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.rest;

import com.zony.app.domain.Notification;
import com.zony.app.domain.vo.NotificationFormVo;
import com.zony.app.service.NotificationService;
import com.zony.app.service.criteria.NotificationQueryCriteria;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import com.zony.common.service.LocalStorageService;
import com.zony.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/13 -10:21
 */

@RestController
@RequiredArgsConstructor
@Api(tags = "消息：消息通知")
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final LocalStorageService localStorageService;
    private static final String ENTITY_NAME = "notification";
    @Log("新建消息")
    @ApiOperation("新建消息")
    @PostMapping(value = "/add")
    @PreAuthorize("@el.check('notification:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody NotificationFormVo resources){
        if (resources.getFormDtoObj().getId() != null) {
            throw new BadRequestException(ENTITY_NAME +" existed.");
        }
        notificationService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("更新接口：逻辑删除消息、已删除消息还原、更新消息，标记已读未读")
    @ApiOperation("更新接口：逻辑删除消息、已删除消息还原、更新消息，标记已读未读")
    @PutMapping(value = "/updateFlag")
    @PreAuthorize("@el.check('notification:updateFlag')")
    public ResponseEntity<Object> updateFlag(@RequestBody Map<String,Object> param){
        List<Integer> objectIds = (List<Integer>) MapUtils.getObject(param,"ids");
        //if (ObjectUtils.isEmpty(objectIds)){
        //    throw new BadRequestException("Set ids is empty! ");
        //}

        Set<Long> ids = objectIds.stream().map(i->Long.valueOf(i.toString())).collect(Collectors.toSet());
        if(param.containsKey("isDelete")){
            notificationService.updateIsDelete(ids,MapUtils.getBoolean(param,"isDelete"));
        }else if(param.containsKey("readFlag")){
            notificationService.updateReadFlag(ids,true);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("更新接口：更新草稿消息内容")
    @ApiOperation("更新接口：更新草稿消息")
    @PutMapping(value = "/update")
    @PreAuthorize("@el.check('notification:update')")
    public ResponseEntity<Object> update(@RequestBody NotificationFormVo resources){
        if (resources.getFormDtoObj().getReadFlag() != null){
            throw new BadRequestException("消息已经发送，无法修改");
        }
        notificationService.update(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除消息")
    @ApiOperation("删除消息")
    @DeleteMapping(value = "/delete")
    @PreAuthorize("@el.check('notification:delete')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        if (CollectionUtils.isEmpty(ids)){
            throw new BadRequestException("Set ids is empty! ");
        }
        notificationService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("按照公告id,题目,内容，备注，有效时间查询公告")
    @ApiOperation("按照公告id,题目,内容，备注，有效时间查询公告")
    @GetMapping(value = "/list")
    public ResponseEntity<Object> list(NotificationQueryCriteria criteria, Pageable pageable){
        if("send".equals(criteria.getListFlag())){
            criteria.setCreateBy(SecurityUtils.getCurrentUsername());
        }else if ("receive".equals(criteria.getListFlag())){
            criteria.setToSomeone("%," + SecurityUtils.getCurrentUsername() + ",%");
        }else { //未匹配上默认查收件
            criteria.setToSomeone("%," + SecurityUtils.getCurrentUsername() + ",%");
        }
        //System.out.println("#######################"+criteria.toString());
        //System.out.println(String.valueOf(SecurityUtils.getCurrentUserId()));
        return new ResponseEntity<>(notificationService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @Log("统计未读消息条数")
    @ApiOperation("统计未读消息条数")
    @GetMapping(value = "/count")
    public ResponseEntity<Object> count(){
        return new ResponseEntity<>(notificationService.count(), HttpStatus.OK);
    }
    //@Log("逻辑删除消息")
    //@ApiOperation("逻辑删除消息")
    //@PutMapping(value = "/updateIsDelete")
    //@PreAuthorize("@el.check('notification:updateIsDelete')")
    //public ResponseEntity<Object> logicDelete(@RequestBody Set<Long> ids){
    //    if (CollectionUtils.isEmpty(ids)){
    //        throw new BadRequestException("Set ids is empty! ");
    //    }
    //    notificationService.updateIsDelete(ids,true);
    //    return new ResponseEntity<>(HttpStatus.OK);
    //}



    //@Log("已删除消息还原")
    //@ApiOperation("已删除消息还原")
    //@PutMapping(value = "/revert")
    //@PreAuthorize("@el.check('notification:revert')")
    //public ResponseEntity<Object> revert(@RequestBody Set<Long> ids){
    //    if (CollectionUtils.isEmpty(ids)){
    //        throw new BadRequestException("Set ids is empty! ");
    //    }
    //    notificationService.updateIsDelete(ids,false);
    //    return new ResponseEntity<>(HttpStatus.OK);
    //}
    //
    //@Log("更新消息，标记已读未读")
    //@ApiOperation("更新消息")
    //@PutMapping(value = "/updateReadFlag")
    //@PreAuthorize("@el.check('notification:updateReadFlag')")
    //public ResponseEntity<Object> updateReadFlag(@RequestBody Set<Long> ids){
    //    if (CollectionUtils.isEmpty(ids)){
    //        throw new BadRequestException("Set ids is empty! ");
    //    }
    //    notificationService.updateReadFlag(ids,true);
    //    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //}


}
