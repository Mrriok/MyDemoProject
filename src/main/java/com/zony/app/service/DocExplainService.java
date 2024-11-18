/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.zony.app.domain.DocExplain;
import com.zony.app.repository.DocExplainRepository;
import com.zony.app.service.criteria.DocExplainQueryCriteria;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/18 -14:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "docExplain")
public class DocExplainService {
    private final DocExplainRepository docExplainRepository;
    private final RedisUtils redisUtils;

    public Object queryAll(DocExplainQueryCriteria criteria, Pageable pageable) {
        Page<DocExplain> page = docExplainRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Cacheable(key = "'id:' + #p0")
    public Object view(Long id) {
        return docExplainRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(DocExplain docExplain) {
        if (!ObjectUtils.isEmpty(docExplainRepository.findByValueAndCompanyName(docExplain.getValue(),docExplain.getCompanyName()))){
            throw new BadRequestException("您添加的 "+docExplain.getValue()+" , 系统中已经存在~");
        }
        docExplainRepository.save(docExplain);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "'id:' + #p0.id")
    public void update(DocExplain docExplain) {
        docExplainRepository.save(docExplain);
    }


    public void delete(Set<Long> ids) {
        docExplainRepository.deleteByIdIn(ids);
        redisUtils.delByKeys("docExplain::id:",ids);//删除缓存
    }
}
