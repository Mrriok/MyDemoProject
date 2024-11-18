/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.domain;

import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/1 -16:26
 */
@Entity
@Getter
@Setter
@Table(name="app_feedback_opinion")
public class FeedbackOpinion extends BaseEntity implements Serializable {
    @Id
    @Column(name = "feedback_id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "意见提出部门")
    private Dept initDept;

    @OneToOne
    @JoinColumn(name = "username")
    @ApiModelProperty(value = "意见提出人")
    private User initUser;

    @ApiModelProperty(value = "章节")
    @Column(name = "chapter", length = 100)
    private String chapter;

    @ApiModelProperty(value = "意见内容")
    @Column(name = "opinion_content", length = 500)
    private String opinionContent;


    @ApiModelProperty(value = "采纳结果")
    @Column(name = "agree_flag")
    private Boolean agreeFlag;

    @ApiModelProperty(value = "拒绝理由")
    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @NotNull
    @ApiModelProperty(value = "关联流程id")
    @Column(name = "workflow_common_id")
    private Long workflowCommonId;

    @NotNull
    @Column(name = "step_seq_num")
    @ApiModelProperty(value = "关联流程节点num")
    private Integer stepSeqNum;

    @Column(name = "step_length")
    @ApiModelProperty(value = "关联流程节点length")
    private Integer stepLength;

    @NotBlank
    @Column(name = "feedback_type_index")
    @ApiModelProperty(value = "反馈意见分类索引")
    private String feedbackTypeIndex;

}
