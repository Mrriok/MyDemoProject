package com.zony.app.rest;

import com.zony.app.domain.DictDetail;
import com.zony.app.domain.Regulation;
//import com.zony.app.service.RegulationService;
import com.zony.app.repository.DeptRepository;
import com.zony.app.repository.DictDetailRepository;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.RegulationService;
import com.zony.app.service.criteria.RegulationQueryCriteria;
import com.zony.app.service.dto.RegulationDto;
import com.zony.app.utils.ExcelUtil;
import com.zony.app.utils.PageableUtil;
import com.zony.common.annotation.Log;
import com.zony.common.exception.BadRequestException;
import com.zony.common.utils.SecurityUtils;
import com.zony.common.utils.StringUtils;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "制度制修订计划")
@RequestMapping("/api/regulation")
public class RegulationController {
    private final RegulationService regulationService;
    private final DictDetailRepository dictDetailRepository;
    private final UserRepository userRepository;
    private final DeptRepository deptRepository;
    @Log("新增制度制修订计划")
    @ApiOperation("新增制度制修订计划")
    @PostMapping
    @PreAuthorize("@el.check('regulation:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RegulationDto resources){
        regulationService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Log("修改计划")
    @ApiOperation("修改计划")
    @PutMapping
    @PreAuthorize("@el.check('regulation:edit')")
    public ResponseEntity<Object> update( @Validated (Regulation.Update.class) @RequestBody RegulationDto resources){
        regulationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除计划")
    @ApiOperation("删除计划")
    @DeleteMapping
    @PreAuthorize("@el.check('regulation:delete')")
    public ResponseEntity<Object> delete( @RequestBody Set<Long> ids ){
        if (CollectionUtils.isEmpty(ids)){
            throw new BadRequestException("Set is empty! ");
        }
        //try {
        regulationService.delete(ids);
        //}catch (Exception e){
        //    throw new BadRequestException("删除数据中存在被其他数据引用的记录，请清除子记录后删除！");
        //}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查询计划")
    @ApiOperation("查询计划")
    @GetMapping
    @PreAuthorize("@el.check('regulation:list')")
    public ResponseEntity<Object> query(RegulationQueryCriteria criteria,Pageable pageable){
        dataFilterByDept(criteria);
        return new ResponseEntity<>(regulationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询计划详情")
    @ApiOperation("查询计划详情")
    @PostMapping("/listInfo")
    @PreAuthorize("@el.check('regulation:listInfo')")
    public ResponseEntity<Object> queryInfo( @RequestBody RegulationQueryCriteria criteria){
        dataFilterByDept(criteria);
        List<String> sort = criteria.getSort();
        String sortKey = "id";
        String order = "DESC";
        if(!CollectionUtils.isEmpty(sort)){
            List<String> sortList = Arrays.asList(sort.get(0).split(","));
            sortKey = sortList.get(0);
            order = sortList.get(1);
        }
        Pageable pageable = PageableUtil.getPageable(criteria.getPage(),criteria.getSize(),sortKey,order);
        //criteria.setStatus(ProgressStatusEnum.COMPLETE);
        return new ResponseEntity<>(regulationService.queryAllInfo(criteria,pageable),HttpStatus.OK);
    }

    @Log("查看计划")
    @ApiOperation("查看计划")
    @GetMapping("/view")
    @PreAuthorize("@el.check('regulation:view')")
    public ResponseEntity<Object> view(@RequestParam @NotNull Long id){
        return new ResponseEntity<>(regulationService.findById(id),HttpStatus.OK);
    }

    @Log("发起编修计划")
    @ApiOperation("编修计划")
    @PutMapping("/operating")
    @PreAuthorize("@el.check('regulation:operating','')")
    public ResponseEntity<Object> operating(@RequestBody RegulationQueryCriteria criteria){
        //废除计划，取id操作
        regulationService.operating(criteria);
        //待补充站内提醒包括邮件提醒
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出编修计划记录")
    @ApiOperation("导出编修计划记录")
    @PostMapping("/exportRegulationPlan")
    @PreAuthorize("@el.check('regulation:exportRegulationPlan')")
    public void exportRegulationPlan(@RequestBody Map<String,Object> param, HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> mp = new HashMap<>();
            //log.info(testDrive.toString());
            String fileName = new String();
            mp.put("exportData", getExcelList(param));
            List<DictDetail> dictDetailList = dictDetailRepository.findByDictName("template_name");
            for (DictDetail item : dictDetailList){
                if ("regulation_template".equals(item.getValue())) {
                    fileName = item.getLabel();
                    break;
                }
            }
            ServletOutputStream out = response.getOutputStream();
            InputStream inputStream = RegulationController.class.getClassLoader()
                    .getResourceAsStream("public/excelTemplate/regulationPlanTemplate.xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
            ExcelUtil util = new ExcelUtil();
            util.exportExcel(inputStream, mp,out);
            out.flush();
            out.close();

            //response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //response.setHeader("Content-Disposition", "attachment; filename=" + "审批记录" + ".xlsx");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Log("获取导出编修计划记录")
    @ApiOperation("获取导出编修计划记录")
    @PostMapping("/getExcelList")
    @PreAuthorize("@el.check('workbench:getExcelList')")
    public List getExcelList(@RequestBody Map<String,Object> param){
        Object regulationIds = MapUtils.getObject(param,"ids");

        //判空
        if(!ObjectUtils.isEmpty(regulationIds)){
            //去重和构建查询参数
            //List<WorkflowCommon> workflowCommonList = new ArrayList<>();
            List<Integer> regulationIntegerIds = (List<Integer>) regulationIds;
            Set<Long> ids = regulationIntegerIds.stream().map(i->Long.valueOf(i.toString())).collect(Collectors.toSet());

            return regulationService.getExcelList(ids);
        }else {
            throw new BadRequestException("您尚无选择任何制度编制计划数据~");
        }
    }

    private void dataFilterByDept(RegulationQueryCriteria criteria) {
        Long deptId = userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername());
        String riskControl = dictDetailRepository.findByDictNameAndLabel("not_filter_data_dept","risk_control").getValue();
        List<Long> riskControlIds = new ArrayList<>();
        if (!StringUtils.isEmpty(riskControl)){
            List<String> riskControlIntegerIds = Arrays.asList(riskControl.split(","));
            riskControlIds = riskControlIntegerIds.stream().map(i->Long.valueOf(i)).collect(Collectors.toList());
        }
        if (!riskControlIds.contains(deptId)){
            criteria.setDeptId(userRepository.findDeptIdByUsername(SecurityUtils.getCurrentUsername()));
        }//风控部门包含当前deptId需要处理
    }
}
