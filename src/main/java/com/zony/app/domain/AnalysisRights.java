package com.zony.app.domain;

import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: 权限手册事项权限实体类
 * @Date 2020-09-21
 * @Author gubin
 */
@Entity
@Getter
@Setter
@Table(name="app_analysis_rights")
public class AnalysisRights extends BaseEntity implements Serializable {
    @Id
    @Column(name = "analysis_rights_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ManyToOne
    @ApiModelProperty(value = "所属事项")
    @JoinColumn(name = "analysis_item_id")
    private AnalysisItem analysisItem;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色权限")
    private String roleRights;

}
