package com.zony.app.domain;

import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jinweiwei
 * @date 2020/8/29
 */

@Entity
@Getter
@Setter
@Table(name="APP_REGULATIONSORT")
public class Regulationsort extends BaseEntity implements Serializable{

    @Id
    @Column(name = "regulationsort_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "上级部门")
    private Long pid;

    @ApiModelProperty(value = "子节点数目", hidden = true)
    private Integer subCount = 0;

    @ApiModelProperty(value = "制度分类名称")
    private String name;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "排序")
    private Integer regulationSort;

    @ApiModelProperty(value = "文件用id")
    private String menuId;
}
