/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.zony.app.domain.Notice;
import com.zony.app.repository.NoticeRepository;
import com.zony.app.service.criteria.NoticeQueryCriteria;
import com.zony.app.service.mapstruct.NoticeMapper;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.RedisUtils;
import com.zony.common.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/8 -16:06
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "notice")
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final RedisUtils redisUtils;

    @Transactional(rollbackFor = Exception.class)
    public void create(Notice resources) {
        noticeRepository.save(resources);
    }

    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Notice resources) {
        ValidationUtil.isNull(resources.getId(),"Notice","id",resources.getId());
        noticeRepository.save(resources);
    }

    //@CacheEvict(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        noticeRepository.deleteAllByIdIn(ids);
        redisUtils.delByKeys("notice::id:",ids);//删除缓存
    }


    @Cacheable(key = "'id:' + #p0.id")
    public Object queryAll(NoticeQueryCriteria criteria, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.ASC, "effectiveTime");
        Page<Notice> page = noticeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(noticeMapper::toDto));
    }

}
