package com.zony.app.domain;

import com.zony.app.enums.WorkflowDefineTypeEnum;
import com.zony.app.enums.WorkflowDisposeObjEnum;
import com.zony.app.enums.WorkflowClassEnum;
import com.zony.app.enums.WorkflowDisposeWayEnum;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "app_workflow_define")
public class WorkflowDefine extends BaseEntity implements Serializable {

    @Id
    @Column(name = "workflow_define_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotNull
    @Column(name = "workflow_type", length = 100)
    @Convert(converter = WorkflowClassEnum.EnumConverter.class)
    @ApiModelProperty(value = "流程类型,如：Borrow")
    private WorkflowClassEnum workflowType;

    @NotBlank
    @Column(name = "step_name", length = 100)
    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @NotNull
    @Column(name = "step_seq_num")
    @ApiModelProperty(value = "步骤序号")
    private Integer stepSeqNum;

    @NotBlank
    @Column(name = "dispose_obj_id", length = 1000)
    @ApiModelProperty(value = "处理者对象id（多值，json数组）")
    private String disposeObjId;

    @NotNull
    @Column(name = "dispose_obj_type", length = 100)
    @Convert(converter = WorkflowDisposeObjEnum.EnumConverter.class)
    @ApiModelProperty(value = "处理者对象类型")
    private WorkflowDisposeObjEnum disposeObjType;

    @NotNull
    @Column(name = "dispose_way_type", length = 100)
    @Convert(converter = WorkflowDisposeWayEnum.EnumConverter.class)
    @ApiModelProperty(value = "步骤处理方式类型")
    private WorkflowDisposeWayEnum disposeWayType;

    @NotNull
    @Column(name = "define_type", length = 100)
    @Convert(converter = WorkflowDefineTypeEnum.EnumConverter.class)
    @ApiModelProperty(value = "步骤处理方式类型")
    private WorkflowDefineTypeEnum defineType;
}
