/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.domain.Dept;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstPropertyEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.SystemNameEnum;
import com.zony.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Convert;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/30 -13:54 
 */
@Data
public class EstablishmentDto extends BaseDTO implements Serializable {
    /**
     * 编制id
     */
    private Long id;

    /**
     * 制度编号
     */
    private String instCode;

    /**
     * 制度名称
     */
    private String instName;

    /**
     * 制度层级
     */
    private InstLevelEnum instLevel;

    /**
     * 制度属性
     */
    private InstPropertyEnum instProperty;
    /**
     * 发起部门
     */
    private Dept initDept;

    /**
     * 编写日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp dateOfWriting;

    /**
     * 发起人
     */
    private UserDto organizer;

    /**
     * 状态
     */
    private InstStatusEnum status;

    /**
     * 关联的编修计划
     */
    private RegulationDto regulationPlan;

    /**
     * 体系名称
     */
    private SystemNameEnum systemName;

    /**
     * 批准人
     */
    private UserDto accepter;

    /**
     * 批准依据
     * 字典还是制度库未确定
     */
    private String reasonForAccept;

    /**
     * 编写部门
     */
    private DeptDto writingDept;

    /**
     * 发布范围
     * 是否为枚举，未确定
     */
    private String scope;

    /**
     * 适用范围
     *  与多个dict对应
     */
    private List<DictDto> suitableScopeDict;

    /**
     * 版本
     *
     */
    private String version;

    /**
     * 参照制度
     * 与制度库制度对应，制度实体未定义
     */
    private Long referenceInstId;

    /**
     * 废除制度
     * 与制度库制度对应，制度实体未定义
     */
    private Long abolitionInstId;

    /**
     * 目的
     *
     */
    private String purpose;

    /**
     * 编制依据
     * 与制度库制度对应，制度实体未定义
     */
    private String preparationBasisIds;//List<Long> preparationBasis;

    /**
     * 主要应对风险
     * 与多个dictDetailDto对应
     */
    private List<DictDetailDto> mainRiskDicts;//List<Long> mainRisks;

    /**
     * 主要应对目标
     * 与多个dictDetailDto对应
     */
    private List<DictDetailDto> mainTargetDicts;//List<Long> mainTargets;

    /**
     * 释义
     * 与多个dictDetailDto对应
     */
    private List<DictDetailDto> meaningDicts;//List<Long> meaning;

    /**
     * 附件
     *
     */
    private List<Object> attachmentFile;//List<Long> attachment;
}
