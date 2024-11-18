package com.zony.app.service;

import com.zony.app.domain.Metadata;
import com.zony.app.repository.MetadataRepository;
import com.zony.app.service.criteria.MetadataQueryCriteria;
import com.zony.app.service.mapstruct.MetadataMapper;
import com.zony.common.exception.EntityExistException;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "metadata")
public class MetadataService {

    private final MetadataRepository metadataRepository;
    private final MetadataMapper metadataMapper;

    @Transactional(rollbackFor = Exception.class)
    public void create(Metadata metadata) {
        metadataRepository.save(metadata);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Metadata resources) {
        Metadata metadata = metadataRepository.findById(resources.getId()).orElseGet(Metadata::new);
        if (ObjectUtils.isEmpty(metadata)) {
            throw new EntityExistException(Metadata.class, "id", resources.getId() + "");
        }
        metadataRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> list) {
        Set<Long> ids = new HashSet<>();
//权限控制:是否能删除发起的计划
        metadataRepository.deleteAllByIdIn(list);
    }

    public Object queryAll(MetadataQueryCriteria queryCriteria, Pageable pageable) {
        Page<Metadata> page = metadataRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(metadataMapper::toDto));
    }
}
