/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service;

import com.zony.app.domain.DocReplyRiskTarget;
import com.zony.app.repository.DocReplyRiskTargetRepository;
import com.zony.app.service.criteria.DocReplyRiskTargetQueryCriteria;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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
 * @date 2020/9/18 -13:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "docReplyRiskTarget")
public class DocReplyRiskTargetService {
    private final RedisUtils redisUtils;
    private final DocReplyRiskTargetRepository docReplyRiskTargetRepository;

    public Object queryAll(DocReplyRiskTargetQueryCriteria criteria, Pageable pageable) {
        Page<DocReplyRiskTarget> page = docReplyRiskTargetRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    public Object view(Long id) {
        return docReplyRiskTargetRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(DocReplyRiskTarget docReplyRiskTarget) {
        if (!ObjectUtils.isEmpty(docReplyRiskTargetRepository.findByValueAndCompanyName(docReplyRiskTarget.getValue(),docReplyRiskTarget.getCompanyName()))){
            throw new BadRequestException("您添加的 "+docReplyRiskTarget.getValue()+" , 系统中已经存在~");
        }
        docReplyRiskTargetRepository.save(docReplyRiskTarget);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "'id:' + #p0.id")
    public void update(DocReplyRiskTarget docReplyRiskTarget) {
        docReplyRiskTargetRepository.save(docReplyRiskTarget);
    }

    public void delete(Set<Long> ids) {
        docReplyRiskTargetRepository.deleteByIdIn(ids);
        redisUtils.delByKeys("docReplyRiskTarget::id:",ids);//删除缓存
    }
}
