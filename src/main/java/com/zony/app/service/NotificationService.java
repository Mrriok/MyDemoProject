/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.zony.app.domain.Notification;
import com.zony.app.domain.Opinion;
import com.zony.app.domain.Regulation;
import com.zony.app.domain.User;
import com.zony.app.domain.vo.NotificationFormVo;
import com.zony.app.domain.vo.OpinionFormVo;
import com.zony.app.repository.NotificationRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.NotificationQueryCriteria;
import com.zony.app.service.dto.NotificationDto;
import com.zony.app.service.dto.OpinionDto;
import com.zony.app.service.dto.RegulationDto;
import com.zony.app.service.mapstruct.NotificationMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/13 -10:09
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "notification")
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final RedisUtils redisUtils;

    //@CacheEvict
    @Transactional(rollbackFor = Exception.class)
    public void create(NotificationFormVo resources) {
        Notification notification = voToEntity(resources);
        notification.setIsDelete(false);
        notificationRepository.save(voToEntity(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReadFlag(Set<Long> ids,Boolean readFlag) {
        List<Notification> notificationList = notificationRepository.findAllByIdIn(ids);
        notificationList.forEach(notification -> notification.setReadFlag(readFlag));
        //redisUtils.delByKeys("notification::id:",ids);
        notificationRepository.saveAll(notificationList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateIsDelete(Set<Long> ids,Boolean isDelete) {
        List<Notification> notificationList = notificationRepository.findAllByIdIn(ids);
        notificationList.forEach(notification -> notification.setIsDelete(isDelete));
        //redisUtils.delByKeys("notification::id:",ids);
        notificationRepository.saveAll(notificationList);
    }
    public void update(NotificationFormVo resources) {
        notificationRepository.save(voToEntity(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        notificationRepository.deleteAllByIdIn(ids);
        //redisUtils.delByKeys("notification::id:",ids);//删除缓存
    }

    //@Cacheable(key = "'id:' + #p0.id")
    public Object queryAll(NotificationQueryCriteria criteria, Pageable pageable) {
        //Sort sort = new Sort(Sort.Direction.ASC, "sentTime");
        Map<String,Object> resultMap = new HashMap<>();
        Page<Notification> page = notificationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<Notification> notificationList = page.getContent();
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(notificationList)){
            notificationList.forEach(item->{
                notificationDtoList.add(entityToDto(item));
            });
        }
        resultMap.put("content",notificationDtoList);
        resultMap.put("totalElements",page.getTotalElements());
        return resultMap;
    }

    public Object count() {
        return notificationRepository.countByToSomeoneAndReadFlag("%," + SecurityUtils.getCurrentUsername() + ",%");
    }

    public Notification voToEntity(NotificationFormVo notificationFormVo){
        //转换基本信息
        NotificationDto notificationDto = notificationFormVo.getFormDtoObj();
        Notification notification = notificationMapper.toEntity(notificationDto);
        StringBuilder usernames = new StringBuilder();
        if(!CollectionUtils.isEmpty(notificationDto.getToSomeoneList())){
            notificationDto.getToSomeoneList().forEach(item->{
                usernames.append(",").append(item.getUsername());
            });
            usernames.append(",");
        }else {
            notification.setToSomeone(null);
        }
        notification.setToSomeone(usernames.toString());
        return notification;
    }
    //public NotificationFormVo entityToVo(Opinion opinion){
    //    OpinionFormVo opinionFormVo = new OpinionFormVo();
    //    OpinionDto opinionDto = opinionMapper.toDto(opinion);
    //
    //    //转换联系人
    //    JsonUtil jsonUtil = JsonUtil.getInstance();
    //    if (!StringUtils.isEmpty(opinion.getContactPersonIds())){
    //        List<String> usernames = (List<String>) jsonUtil.json2obj(opinion.getContactPersonIds(),List.class);
    //        opinionDto.setContactPersonList(userRepository.findByUsernameIn(usernames));
    //    }else {
    //        opinionDto.setContactPersonList(new ArrayList<>());
    //    }
    //    opinionFormVo.setFormDtoObj(opinionDto);
    //    return opinionFormVo;
    //}
    public NotificationDto entityToDto(Notification notification){

        NotificationDto notificationDto = notificationMapper.toDto(notification);
        List<String> usernames = Arrays.asList(notification.getToSomeone().split(","));
        if (!CollectionUtils.isEmpty(usernames)){
            notificationDto.setToSomeoneList(userRepository.findByUsernameIn(usernames));
        }else {
            notificationDto.setToSomeoneList(new ArrayList<>());
        }
        if(!StringUtils.isEmpty(notification.getCreateBy())){
            notificationDto.setInitUser(userRepository.findByUsername(notification.getCreateBy()));
        }else {
            notificationDto.setInitUser(new User());
        }
        return notificationDto;
    }

}
