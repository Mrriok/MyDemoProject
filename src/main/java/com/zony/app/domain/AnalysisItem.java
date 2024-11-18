package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @Description: 权限手册事项信息实体类
 * @Date 2020-09-21
 * @Author gubin
 */
@Entity
@Getter
@Setter
@Table(name = "app_analysis_item")
public class AnalysisItem extends BaseEntity implements Serializable {
    @Id
    @Column(name = "analysis_item_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ManyToOne
    @ApiModelProperty(value = "所属审批备案事项组")
    @JoinColumn(name = "analysis_group_id")
    private AnalysisGroup analysisGroup;

    @OneToMany(mappedBy = "analysisItem", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @ApiModelProperty(value = "审批备案事项", hidden = true)
    private Set<AnalysisRights> analysisRights;

    @ApiModelProperty(value = "审批事项序号")
    private String itemSeq;

    @ApiModelProperty(value = "审批事项名称")
    private String itemName;

    @ApiModelProperty(value = "编制依据")
    private String according;

    @ApiModelProperty(value = "审批时限要求")
    private String timeLimit;
}
