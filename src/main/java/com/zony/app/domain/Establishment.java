/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.*;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/29 -16:33
 */
@Entity
@Getter
@Setter
@Table(name="app_establishment")
public class Establishment extends BaseEntity implements Serializable {

    @Id
    @Column(name = "establishment_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "制度编号")
    private String instCode;

    @NotBlank
    @ApiModelProperty(value = "制度名称")
    private String instName;

    //@Column(name = "init_level", length = 200)
    @Convert(converter = InstLevelEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度层级")
    private InstLevelEnum instLevel;

    @Convert(converter = InstPropertyEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度属性")
    private InstPropertyEnum instProperty;

    @OneToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "发起部门")
    private Dept initDept;

    //@NotNull
    @ApiModelProperty(value = "编写日期")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp dateOfWriting;

    @OneToOne
    @JoinColumn(name = "username")
    @ApiModelProperty(value = "发起人")
    private User organizer;

    @NotNull
    @ApiModelProperty(value = "状态")
    @Convert(converter = InstStatusEnum.EnumConverter.class)
    private InstStatusEnum status;

    @OneToOne
    @JoinColumn(name = "regulation_id")
    @ApiModelProperty(value = "关联的编修计划")
    private Regulation regulationPlan;

    @NotNull
    @ApiModelProperty(value = "体系名称")
    @Convert(converter = SystemNameEnum.EnumConverter.class)
    private SystemNameEnum systemName;

    @OneToOne
    @JoinColumn(name = "username_b")
    @ApiModelProperty(value = "批准人")
    private User accepter;

    //字典还是制度库未确定
    @ApiModelProperty(value = "批准依据")
    private String reasonForAccept;

    @OneToOne
    @JoinColumn(name = "dept_id_b")
    @ApiModelProperty(value = "编写部门")
    private Dept writingDept;

    //是否为枚举，未确定
    @NotBlank //数据字典
    @ApiModelProperty(value = "发布范围")
    private String scope;

    //与dict中多个id对应
    @NotBlank  //数据字典
    @ApiModelProperty(value = "适用范围")
    private String suitableScopeDictId;

    @NotBlank
    @ApiModelProperty(value = "版本")
    private String version;

    //与制度库制度对应，制度实体未定义
    @ApiModelProperty(value = "参照制度")
    private Long referenceInstId;

    //与制度库制度对应，制度实体未定义
    @ApiModelProperty(value = "废除制度")
    private Long abolitionInstId;

    @NotBlank
    @ApiModelProperty(value = "目的")
    private String purpose;

    //与制度库制度对应，制度实体未定义
    @ApiModelProperty(value = "编制依据")
    private String preparationBasisId;//List<Long> preparationBasis;

    @NotBlank
    //与dictDetail中多个id对应  //数据字典
    //@JoinColumn(name = "detail_id_b")
    @ApiModelProperty(value = "主要应对风险")
    private String mainRiskDictIds;//List<Long> mainRisks;

    @NotBlank
    //与dictDetail中多个id对应 //数据字典
    @ApiModelProperty(value = "主要应对目标")
    private String mainTargetDictIds;//List<Long> mainTargets;

    @NotBlank
    //与dictDetail中多个id对应 //数据字典
    @ApiModelProperty(value = "释义")
    private String meaningDictIds;//List<Long> meaning;

    //数据字典
    @ApiModelProperty(value = "附件")
    private String attachmentFileIds;//List<Long> attachment;

    @ApiModelProperty(value = "分发给的人")
    private String reminder;
}
