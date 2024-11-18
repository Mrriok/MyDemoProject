
package com.zony.app.service;

import com.zony.app.repository.DictDetailRepository;
import com.zony.app.repository.DictRepository;
import com.zony.app.service.mapstruct.DictDetailMapper;
import lombok.RequiredArgsConstructor;
import com.zony.app.domain.Dict;
import com.zony.app.domain.DictDetail;
import com.zony.app.service.criteria.DictDetailQueryCriteria;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.QueryHelp;
import com.zony.common.utils.RedisUtils;
import com.zony.common.utils.ValidationUtil;
import com.zony.app.service.dto.DictDetailDto;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dict")
public class DictDetailService {

    private final DictRepository dictRepository;
    private final DictDetailRepository dictDetailRepository;
    private final DictDetailMapper dictDetailMapper;
    private final RedisUtils redisUtils;


    public Map<String,Object> queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {
        Page<DictDetail> page = dictDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dictDetailMapper::toDto));
    }


    @Transactional(rollbackFor = Exception.class)
    public void create(DictDetail resources) {
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        DictDetail dictDetail = dictDetailRepository.findById(resources.getId()).orElseGet(DictDetail::new);
        ValidationUtil.isNull( dictDetail.getId(),"DictDetail","id",resources.getId());
        resources.setId(dictDetail.getId());
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }


    @Cacheable(key = "'name:' + #p0")
    public List<DictDetailDto> getDictByName(String name) {
        return dictDetailMapper.toDto(dictDetailRepository.findByDictName(name));
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DictDetail dictDetail = dictDetailRepository.findById(id).orElseGet(DictDetail::new);
        // 清理缓存
        delCaches(dictDetail);
        dictDetailRepository.deleteById(id);
    }

    public void delCaches(DictDetail dictDetail){
        Dict dict = dictRepository.findById(dictDetail.getDict().getId()).orElseGet(Dict::new);
        redisUtils.del("dept::name:" + dict.getName());
    }

}
