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
 * @Description: 权限手册事项组实体类
 * @Date 2020-09-21
 * @Author gubin
 */
@Entity
@Getter
@Setter
@Table(name = "app_analysis_group")
public class AnalysisGroup extends BaseEntity implements Serializable {
    @Id
    @Column(name = "analysis_group_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ManyToOne
    @ApiModelProperty(value = "所属权限手册表格")
    @JoinColumn(name = "analysis_table_id")
    private AnalysisTable analysisTable;

    @OneToMany(mappedBy = "analysisGroup", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @ApiModelProperty(value = "审批备案事项", hidden = true)
    private Set<AnalysisItem> analysisItemSet;

    @ApiModelProperty(value = "审批备案事项组序号")
    private String groupSeq;

    @ApiModelProperty(value = "审批备案事项组名称")
    private String groupName;


}
