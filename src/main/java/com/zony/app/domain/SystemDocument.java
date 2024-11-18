package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.*;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 制度信息实体类
 *
 * @author gubin
 * @date 2020-08-25
 */
@Entity
@Getter
@Setter
@Table(name = "app_system_document")
public class SystemDocument extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5604731867701307347L;
    @Id
    @Column(name = "system_document_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "regulation_id")
    @ApiModelProperty(value = "编修计划")
    private Regulation regulation;

    @NotNull(groups = Create.class)
    @Column(name = "system_title")
    @ApiModelProperty(value = "制度名称")
    private String systemTitle;

    @JoinColumn(name = "init_dept")
    @ApiModelProperty(value = "发起部门")
    private String initDept;

    @Column(name = "initUser")
    @ApiModelProperty(value = "发起人")
    private String initUser;

    //@Column(name = "init_level", length = 200)
    @Convert(converter = InstLevelEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度层级")
    private InstLevelEnum instLevel;

    @Convert(converter = InstPropertyEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度属性")
    private InstPropertyEnum instProperty;

    @ManyToOne
    @JoinColumn(name = "regulationsort_id")
    @ApiModelProperty(value = "制度类别")
    private Regulationsort regulationsort;

    @Column(name = "institutional_name")
    @ApiModelProperty(value = "体系名称")
    private String institutionalName;

    @NotNull(groups = {Create.class, Update.class})
    @Column(name = "system_code")
    @ApiModelProperty(value = "制度编码")
    private String systemCode;

    @Column(name = "version")
    @ApiModelProperty(value = "版本")
    private String version;

    @Column(name = "modify_code")
    @ApiModelProperty(value = "修改码")
    private String modifyCode;

    @Column(name = "company_name")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @Column(name = "approver")
    @ApiModelProperty(value = "批准人")
    private String approver;

    @Column(name = "according")
    @ApiModelProperty(value = "批准依据")
    private String according;

    @Column(name = "draft_dept")
    @ApiModelProperty(value = "牵头起草部门")
    private String draftDept;

    @Column(name = "publish_range")
    @ApiModelProperty(value = "发布范围")
    private String publishRange;

    @Column(name = "reference")
    @ApiModelProperty(value = "参照制度")
    private String reference;

    @Column(name = "abolish")
    @ApiModelProperty(value = "废除制度")
    private String abolish;

    //@Column(name = "publish_date")
    //@ApiModelProperty(value = "发布日期")
    //@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    //private Timestamp publishDate;
    //
    //@Column(name = "effective_date")
    //@ApiModelProperty(value = "生效日期")
    //@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    //private Timestamp effectiveDate;

    //@NotNull
    @ApiModelProperty(value = "编写日期")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp dateOfWriting;

    @Column(name = "publish_date_str")
    @ApiModelProperty(value = "发布日期")
    private String publishDateStr;

    @Column(name = "effective_date_str")
    @ApiModelProperty(value = "生效日期")
    private String effectiveDateStr;

    @Column(name = "doc_purpose")
    @ApiModelProperty(value = "目的")
    private String docPurpose;

    @Column(name = "doc_apply_range")
    @ApiModelProperty(value = "适用范围")
    private String docApplyRange;

    @Column(name = "doc_according")
    @ApiModelProperty(value = "编制依据")
    private String docAccording;

    @Column(name = "doc_reply_risk_target")
    @ApiModelProperty(value = "主要应对的风险及应对目标")
    private String docReplyRiskTarget;

    @Column(name = "doc_explain")
    @ApiModelProperty(value = "释义")
    private String docExplain;

    @Column(name = "publish_symbol")
    @ApiModelProperty(value = "发布文号")
    private String publishSymbol;

    @Column(name = "inst_status")
    @Convert(converter = InstStatusEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度状态")
    private InstStatusEnum instStatus;

    @Column(name = "inst_status_type")
    @Convert(converter = InstStatusTypeEnum.EnumConverter.class)
    @ApiModelProperty(value = "制度文档状态分类")
    private InstStatusTypeEnum instStatusType;

    @Column(name = "current_version_flag")
    @ApiModelProperty(value = "是否当前版本")
    private Boolean currentVersionFlag;

    @Column(name = "deptId")//对象 systemTitle,systemCode,jsonString
    @ApiModelProperty(value = "制度所属部门id")
    private Long deptId;
}
