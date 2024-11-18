package com.zony.app.service;

import com.zony.app.domain.Collect;
import com.zony.app.domain.SystemDocument;
import com.zony.app.repository.CollectRepository;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.service.criteria.CollectQueryCriteria;
import com.zony.app.service.mapstruct.CollectMapper;
import com.zony.app.utils.ReflectUtil;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "baselirary")
public class CollectService {


    private final CollectRepository collectRepository;
    private final CollectMapper collectMapper;
    private final SystemDocumentRepository systemDocumentRepository;

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> list) {
        Set<Long> ids = new HashSet<>();
        collectRepository.deleteAllByIdIn(list);
    }

    public Object queryAll(CollectQueryCriteria queryCriteria, Pageable pageable) {
        queryCriteria.setUsername(SecurityUtils.getCurrentUsername());
        Page<Collect> page = collectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Set<Long> systemDocumentIds) {
        List<SystemDocument> systemDocumentList = systemDocumentRepository.findAllById(systemDocumentIds);
        List<Collect> collectList = new ArrayList<>();
        String username = SecurityUtils.getCurrentUsername();
        systemDocumentList.forEach(item->{
            Collect collect = new Collect();
            collect.setSystemDocumentId(item.getId());
            //collect.setInitDept(item.getInitDept());
            //collect.setInitUser(item.getInitUser());
            //collect.setSystemCode(item.getSystemCode());
            collect.setSystemTitle(item.getSystemTitle());
            collect.setUsername(username);
            collectList.add(collect);
        });
        collectRepository.saveAll(collectList);
    }

    public Object view(Long id) {
        Collect collect = collectRepository.findById(id).orElseGet(Collect::new);
        if(ObjectUtils.isEmpty(collect)){
            throw new BadRequestException("未查询到相关收藏信息");
        }
        SystemDocument systemDocument = systemDocumentRepository.findById(collect.getSystemDocumentId()).orElseGet(SystemDocument::new);
        Map<String,Object> attributesMap = new HashMap<>();
        attributesMap.put("systemDocument",systemDocument);
        return ReflectUtil.getObject(collect,attributesMap);
    }
}
