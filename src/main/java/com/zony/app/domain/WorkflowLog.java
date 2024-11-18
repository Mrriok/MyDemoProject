package com.zony.app.domain;

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
@Table(name="app_workflow_log")
public class WorkflowLog extends BaseEntity implements Serializable {

    @Id
    @Column(name = "workflow_log_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "workflow_common_id")
    @ApiModelProperty(value = "所属流程")
    private WorkflowCommon workflowCommon;

    @NotNull
    @Column(name = "step_seq_num")
    @ApiModelProperty(value = "步骤序号")
    private Integer stepSeqNum;

    @NotBlank
    @Column(name = "step_name", length = 100)
    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @NotNull
    @Column(name = "username", length = 100)
    @ApiModelProperty(value = "步骤处理人")
    private String username;

    @Column(name = "opinion", length = 500)
    @ApiModelProperty(value = "审批意见")
    private String opinion;

    @Column(name = "agree_flag")
    @ApiModelProperty(value = "审批结果")
    private Boolean agreeFlag;

    @Column(name = "reminder", length = 300)
    @ApiModelProperty(value = "需要提醒的人")
    private String reminder;

    @NotNull
    @Column(name = "step_length")
    @ApiModelProperty(value = "步长")
    private Integer stepLength;

}
