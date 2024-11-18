package com.zony.app.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zony.app.domain.*;
import com.zony.app.repository.RegulationsortRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.criteria.RegulationsortQueryCriteria;
import com.zony.app.service.dto.*;
import com.zony.app.service.mapstruct.RegulationsortMapper;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "regulationsort")
public class RegulationsortService {

    private final RegulationsortRepository regulationsortRepository;
    private final RedisUtils redisUtils;
    private final RegulationsortMapper regulationsortMapper;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public void create(Regulationsort resources) {
        regulationsortRepository.save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        // 清理缓存
        redisUtils.del("regulationsort::pid:" + (resources.getPid() == null ? 0 : resources.getPid()));
        updateSubCnt(resources.getPid());
    }

    private void updateSubCnt(Long deptId){
        if(deptId != null){
            int count = regulationsortRepository.countByPid(deptId);
            regulationsortRepository.updateSubCntById(count, deptId);
        }
    }

    public List<RegulationsortDto> queryAll(RegulationsortQueryCriteria criteria, Boolean isQuery) throws Exception {
        Sort sort = new Sort(Sort.Direction.ASC, "regulationSort");
        if (isQuery) {
            criteria.setPidIsNull(true);
            List<Field> fields = QueryHelp.getAllFields(criteria.getClass(), new ArrayList<>());
            List<String> fieldNames = new ArrayList<String>(){{ add("pidIsNull");add("enabled");}};
            for (Field field : fields) {
                //设置对象的访问权限，保证对private的属性的访问，test
                field.setAccessible(true);
                Object val = field.get(criteria);
                if(fieldNames.contains(field.getName())){
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        return regulationsortMapper.toDto(regulationsortRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),sort));
    }

    @Cacheable(key = "'id:' + #p0")
    public RegulationsortDto findById(Long id) {
        Regulationsort regulationsort = regulationsortRepository.findById(id).orElseGet(Regulationsort::new);
        ValidationUtil.isNull(regulationsort.getId(),"Regulationsort","id",id);
        return regulationsortMapper.toDto(regulationsort);
    }

    public List<RegulationsortDto> getSuperior(RegulationsortDto regulationsortDto, List<Regulationsort> depts) {
        if(regulationsortDto.getPid() == null){
            depts.addAll(regulationsortRepository.findByPidIsNull());
            return regulationsortMapper.toDto(depts);
        }
        depts.addAll(regulationsortRepository.findByPid(regulationsortDto.getPid()));
        return getSuperior(findById(regulationsortDto.getPid()), depts);
    }

    public Object buildTree(List<RegulationsortDto> regulationsortDtos) {
        Set<RegulationsortDto> trees = new LinkedHashSet<>();
        Set<RegulationsortDto> depts= new LinkedHashSet<>();
        List<java.lang.String> regulationsortNames = regulationsortDtos.stream().map(RegulationsortDto::getName).collect(Collectors.toList());
        boolean isChild;
        for (RegulationsortDto regulationsortDto : regulationsortDtos) {
            isChild = false;
            if (regulationsortDto.getPid() == null) {
                trees.add(regulationsortDto);
            }
            for (RegulationsortDto it : regulationsortDtos) {
                if (it.getPid() != null && regulationsortDto.getId().equals(it.getPid())) {
                    isChild = true;
                    if (regulationsortDto.getChildren() == null) {
                        regulationsortDto.setChildren(new ArrayList<>());
                    }
                    regulationsortDto.getChildren().add(it);
                }
            }
            if(isChild) {
                depts.add(regulationsortDto);
            } else if(regulationsortDto.getPid() != null &&  !regulationsortNames.contains(findById(regulationsortDto.getPid()).getName())) {
                depts.add(regulationsortDto);
            }
        }

        if (CollectionUtil.isEmpty(trees)) {
            trees = depts;
        }
        Map<String,Object> map = new HashMap<>(2);
        map.put("totalElements",regulationsortDtos.size());
        map.put("content",CollectionUtil.isEmpty(trees)? regulationsortDtos :trees);
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Regulationsort resources) {
        // 旧的部门
        Long oldPid = findById(resources.getId()).getPid();
        Long newPid = resources.getPid();
        if(resources.getPid() != null && resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Regulationsort regulationsort = regulationsortRepository.findById(resources.getId()).orElseGet(Regulationsort::new);
        ValidationUtil.isNull( regulationsort.getId(),"Dept","id",resources.getId());
        resources.setId(regulationsort.getId());
        regulationsortRepository.save(resources);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
        delCaches(resources.getId(), oldPid, newPid);
    }

    public void delCaches(Long id, Long oldPid, Long newPid){
        List<User> users = userRepository.findByDeptRoleId(id);
        // 删除数据权限
        redisUtils.delByKeys("data::user:",users.stream().map(User::getUserId).collect(Collectors.toSet()));
        redisUtils.del("regulationsort::id:" + id);
        redisUtils.del("regulationsort::pid:" + (oldPid == null ? 0 : oldPid));
        redisUtils.del("regulationsort::pid:" + (newPid == null ? 0 : newPid));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> list) {
        Set<Long> ids = new HashSet<>();
//权限控制:是否能删除发起的计划
        regulationsortRepository.deleteAllByIdIn(list);
    }
}
