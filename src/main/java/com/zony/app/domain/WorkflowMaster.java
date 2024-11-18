package com.zony.app.domain;

import com.zony.app.enums.WorkflowClassEnum;
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
@Table(name = "app_workflow_master")
public class WorkflowMaster extends BaseEntity implements Serializable {
    @Id
    @Column(name = "workflow_master_id")
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

    @Column(name = "workflow_num", length = 100)
    @ApiModelProperty(value = "流程编号,如：JY202005070005")
    private String workflowNum;

    @NotBlank
    @Column(name = "workflow_title", length = 100)
    @ApiModelProperty(value = "流程标题,如：顾斌的借阅流程")
    private String workflowTitle;

    //@OneToOne
    //@JoinColumn(name = "now_step_id")
    @Column(name = "now_step_num")
    @ApiModelProperty(value = "当前处理步骤", hidden = true)
    private Integer nowStepNum;

    @NotNull
    @Column(name = "start_username")
    @ApiModelProperty(value = "流程发起人")
    private Long startUsername;

    @NotNull
    @Column(name = "finish_flag")
    @ApiModelProperty(value = "是否已办结")
    private Boolean finishFlag;

    @NotNull
    @Column(name = "pre_flag")
    @ApiModelProperty(value = "是否返回风控部门初审标志")
    private Boolean preFlag;

    @NotNull
    @Column(name = "handler_num")
    @ApiModelProperty(value = "记录分发人的数量")
    private Integer handlerNum;

    @NotNull
    @Column(name = "step_length")
    @ApiModelProperty(value = "步长")
    private Integer stepLength;
}
