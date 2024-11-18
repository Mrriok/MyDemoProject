package com.zony.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zony.app.enums.*;
import com.zony.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 元数据管理实体类.
 * @author jinweiwei
 * @date 2020/7/17
 */

@Entity
@Getter
@Setter
@Table(name="app_metadata")
public class Metadata extends BaseEntity implements Serializable{

    @Id
    @Column(name = "metadata_id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
    @SequenceGenerator(name = "SEQ_ID", allocationSize = 1, initialValue = 1, sequenceName = "SEQ_ID")
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "主要应对的风险")
    private String risk;

    @ApiModelProperty(value = "制度引用数量")
    private String quote;

    @ApiModelProperty(value = "主要应对的目标")
    private String target;

}
