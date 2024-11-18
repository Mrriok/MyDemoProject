package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.*;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 制度制修订计划实体类.
 *
 * @author jinweiwei
 * @date 2020/7/17
 */

@Entity
@Getter
@Setter
@Table(name = "app_regulation")
public class Regulation extends BaseEntity implements Serializable {

    @Id
    @Column(name = "regulation_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "制度名称")
    private String instName;

    @ApiModelProperty(value = "制度编码")
    private String instCode;

    @OneToOne
    @JoinColumn(name = "username") //牵头起草部门在联系人信息中
    @ApiModelProperty(value = "牵头起草部门联系人")
    private User initUser;

    @Column(name = "publish_range")
    @ApiModelProperty(value = "发布范围")
    private String publishRange;

    @Column(name = "company_name")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    //@Column(name = "reference")//多个id的jsonString
    //@ApiModelProperty(value = "参照制度")
    //private String reference;

    @Column(name = "docAccording")//对象 systemTitle,systemCode,jsonString
    @ApiModelProperty(value = "编制依据")
    private String docAccording;

    @Column(name = "abolish")//多个id的jsonString
    @ApiModelProperty(value = "废除制度")
    private String abolish;

    @Column(name = "reason")//
    @ApiModelProperty(value = "新增/修订原因")
    private String reason;

    @Column(name = "delayReason")//
    @ApiModelProperty(value = "延期原因")
    private String delayReason;

    @Convert(converter = ProgressStatusEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度制修订计划状态")
    private ProgressStatusEnum status;

    @Convert(converter = InstPropertyEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度性质")
    private InstPropertyEnum instProperty;

    @Convert(converter = InstLevelEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度层级")
    private InstLevelEnum instLevel;

    @ManyToOne
    @JoinColumn(name = "regulationsort_id")
    @ApiModelProperty(value = "制度类别")
    private Regulationsort regulationsort;

    @ApiModelProperty(value = "预计完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp expectedTime;

    @Column(name = "deptId")//对象 systemTitle,systemCode,jsonString
    @ApiModelProperty(value = "制度所属部门id")
    private Long deptId;

    @Column(name = "is_submit")
    @ApiModelProperty(value = "是否提交延迟原因")
    private Boolean isSubmit;
}
