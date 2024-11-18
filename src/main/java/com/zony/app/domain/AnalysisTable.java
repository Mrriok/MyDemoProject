package com.zony.app.domain;

import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @Description: 权限手册事项权限实体类
 * @Date 2020-09-21
 * @Author gubin
 */
@Entity
@Getter
@Setter
@Table(name = "app_analysis_table")
public class AnalysisTable extends BaseEntity implements Serializable {
    @Id
    @Column(name = "analysis_table_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ManyToOne
    @ApiModelProperty(value = "所属权限手册")
    @JoinColumn(name = "jurisdiction_id")
    private Jurisdiction jurisdiction;

    @OneToMany(mappedBy = "analysisTable", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @ApiModelProperty(value = "审批备案事项组", hidden = true)
    private Set<AnalysisGroup> analysisGroupSet;

    @ApiModelProperty(value = "审核要点")
    private String keyPoint;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "部门名称")
    private String departName;
}
