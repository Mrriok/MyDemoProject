
package com.zony.app.rest;

import cn.hutool.core.collection.CollectionUtil;
import com.zony.app.domain.Dept;
import com.zony.app.repository.DeptRepository;
import com.zony.app.repository.DictDetailRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.DeptService;
import com.zony.app.service.criteria.DeptQueryCriteria;
import com.zony.app.service.dto.DeptDto;
import com.zony.app.service.mapstruct.DeptMapper;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.PageUtil;
import com.zony.common.utils.SecurityUtils;
import com.zony.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@RequestMapping("/api/dept")
public class DeptController {

    private final DeptService deptService;
    private final DeptRepository deptRepository;
    private final DeptMapper deptMapper;
    private final UserRepository userRepository;
    private final DictDetailRepository dictDetailRepository;
    private static final String ENTITY_NAME = "dept";

    @Log("导出部门数据")
    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    public void download(HttpServletResponse response, DeptQueryCriteria criteria) throws Exception {
        deptService.download(deptService.queryAll(criteria, false), response);
    }

    @Log("查询部门")
    @ApiOperation("查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<Object> query(DeptQueryCriteria criteria) throws Exception {
        Dept dept = new Dept();
        if (criteria.getListFlag() != null && criteria.getListFlag()){
            dataFilterByDept(criteria);//如果是风控部门查全部，其他部门查子分部门
            dept = deptRepository.findById(criteria.getPid()).orElseGet(Dept::new);
        }else {
            criteria.setListFlag(null);
        }

        List<DeptDto> deptDtos = deptService.queryAll(criteria, true);

        if (criteria.getListFlag() != null && criteria.getListFlag()){
            List<DeptDto> deptDtoList = new ArrayList<>();
            deptDtoList.add(deptMapper.toDto(dept));
            deptDtoList.get(0).setChildren(deptDtos);
            return new ResponseEntity<>(PageUtil.toPage(deptDtoList, deptDtos.size()),HttpStatus.OK);
        }
        return new ResponseEntity<>(PageUtil.toPage(deptDtos, deptDtos.size()),HttpStatus.OK);
    }

    @Log("查询部门")
    @ApiOperation("查询部门:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids) {
        Set<DeptDto> deptDtos  = new LinkedHashSet<>();
        for (Long id : ids) {
            DeptDto deptDto = deptService.findById(id);
            List<DeptDto> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            deptDtos.addAll(depts);
        }
        return new ResponseEntity<>(deptService.buildTree(new ArrayList<>(deptDtos)),HttpStatus.OK);
    }

    @Log("新增部门")
    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dept resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        deptService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改部门")
    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    public ResponseEntity<Object> update(@Validated(Dept.Update.class) @RequestBody Dept resources){
        deptService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除部门")
    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        Set<DeptDto> deptDtos = new HashSet<>();
        for (Long id : ids) {
            List<Dept> deptList = deptService.findByPid(id);
            deptDtos.add(deptService.findById(id));
            if(CollectionUtil.isNotEmpty(deptList)){
                deptDtos = deptService.getDeleteDepts(deptList, deptDtos);
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtos);
        deptService.delete(deptDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void dataFilterByDept(DeptQueryCriteria criteria) {
        Long deptId = userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername());
        String riskControl = dictDetailRepository.findByDictNameAndLabel("not_filter_data_dept","risk_control").getValue();
        List<Long> riskControlIds = new ArrayList<>();
        if (!StringUtils.isEmpty(riskControl)){
            List<String> riskControlIntegerIds = Arrays.asList(riskControl.split(","));
            riskControlIds = riskControlIntegerIds.stream().map(i->Long.valueOf(i)).collect(Collectors.toList());
        }
        if (!riskControlIds.contains(deptId)){
            criteria.setPid(userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername()));
        }else { //风控部门查询上级及以下的部门
            //criteria.setPid(userRepository.findPDeptIdByUsername(SecurityUtils.getCurrentUsername()));
        }
    }
}
